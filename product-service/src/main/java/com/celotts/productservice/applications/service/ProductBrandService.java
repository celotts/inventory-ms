package com.celotts.productservice.applications.service;

import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.domain.port.product.ProductBrandPort;
import com.celotts.productservice.domain.port.product_brand.ProductBrandUseCase;
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
import java.util.Optional;
import java.util.UUID;

@Service("productBrandService") // Asegúrate de que coincida con tu @Qualifier
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductBrandService implements ProductBrandPort {

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
        return ProductBrandDtoMapper.toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public List<ProductBrandResponseDto> findAll() {
        return productBrandUseCase.findAll().stream()
                .map(ProductBrandDtoMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductBrandResponseDto findById(UUID id) {
        ProductBrandModel model = productBrandUseCase.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductBrand not found with id: " + id));
        return ProductBrandDtoMapper.toResponseDto(model);
    }

    public ProductBrandResponseDto update(UUID id, ProductBrandUpdateDto dto) {
        ProductBrandModel existing = productBrandUseCase.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductBrand not found with id: " + id));

        productBrandUseCase.findByName(dto.getName())
                .filter(mProductBrandMode -> !mProductBrandMode.getId().equals(id))
                .ifPresent(mProductBrandMode -> {
                    throw new IllegalArgumentException("ProductBrand with name '" + dto.getName() + "' already exists");
                });

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setEnabled(dto.getEnabled());
        existing.setUpdatedBy(dto.getUpdatedBy());
        existing.setUpdatedAt(LocalDateTime.now());

        ProductBrandModel updated = productBrandUseCase.save(existing);
        return ProductBrandDtoMapper.toResponseDto(updated);
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
        return ProductBrandDtoMapper.toResponseDto(model);
    }

    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return productBrandUseCase.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return productBrandUseCase.existsById(id);
    }

    @Transactional(readOnly = true)
    public Optional<String> findNameById(UUID id) {
        return productBrandUseCase.findNameById(id);
    }

    @Transactional(readOnly = true)
    public List<UUID> findAllIds() {
        return productBrandUseCase.findAllIds();
    }

    @Override
    public ProductBrandResponseDto enableBrand(UUID id) {
        var brand = productBrandUseCase.enableBrand(id);
        return ProductBrandDtoMapper.toResponseDto(brand);
    }

    @Override
    public ProductBrandResponseDto disableBrand(UUID id) {
        var brand = productBrandUseCase.disableBrand(id);
        return ProductBrandDtoMapper.toResponseDto(brand);
    }


}