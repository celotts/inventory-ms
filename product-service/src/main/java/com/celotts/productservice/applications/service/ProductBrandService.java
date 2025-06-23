package com.celotts.productservice.applications.service;

import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.domain.port.prodcut_brand.ProductBrandUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productBrand.ProductBrandDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProductBrandService {

    private final ProductBrandUseCase productBrandUseCase;
    private final ProductBrandDtoMapper dtoMapper;

    public ProductBrandResponseDto create(ProductBrandCreateDto createDto) {
        log.info("Creating new ProductBrand with name: {}", createDto.getName());

        if (productBrandUseCase.existsByName(createDto.getName())) {
            throw new IllegalArgumentException("ProductBrand with name '" + createDto.getName() + "' already exists");
        }

        ProductBrandModel model = dtoMapper.toModel(createDto);
        model.setCreatedAt(LocalDateTime.now());
        model.setCreatedBy(createDto.getCreatedBy());

        ProductBrandModel saved = productBrandUseCase.save(model);
        return dtoMapper.toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public List<ProductBrandResponseDto> findAll() {
        return productBrandUseCase.findAll().stream()
                .map(dtoMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductBrandResponseDto findById(UUID id) {
        ProductBrandModel model = productBrandUseCase.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductBrand not found with id: " + id));
        return dtoMapper.toResponseDto(model);
    }

    public ProductBrandResponseDto update(UUID id, ProductBrandUpdateDto dto) {
        ProductBrandModel existing = productBrandUseCase.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductBrand not found with id: " + id));

        productBrandUseCase.findByName(dto.getName())
                .filter(m -> !m.getId().equals(id))
                .ifPresent(m -> {
                    throw new IllegalArgumentException("ProductBrand with name '" + dto.getName() + "' already exists");
                });

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setEnabled(dto.getEnabled());
        existing.setUpdatedBy(dto.getUpdatedBy());
        existing.setUpdatedAt(LocalDateTime.now());

        ProductBrandModel updated = productBrandUseCase.save(existing);
        return dtoMapper.toResponseDto(updated);
    }

    public void delete(UUID id) {
        if (!productBrandUseCase.existsById(id)) {
            throw new RuntimeException("ProductBrand not found with id: " + id);
        }
        productBrandUseCase.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ProductBrandResponseDto findByName(String name) {
        ProductBrandModel model = productBrandUseCase.findByName(name)
                .orElseThrow(() -> new RuntimeException("ProductBrand not found with name: " + name));
        return dtoMapper.toResponseDto(model);
    }

    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return productBrandUseCase.existsByName(name);
    }
}