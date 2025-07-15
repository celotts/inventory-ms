package com.celotts.productservice.applications.service;

import com.celotts.productservice.domain.model.ProductUnitModel;     // caso de uso
import com.celotts.productservice.domain.port.product.unit.input.ProductUnitPort;
import com.celotts.productservice.domain.port.product.unit.input.ProductUnitUseCase;
import com.celotts.productservice.domain.port.product.unit.output.ProductUnitRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.*;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productUnit.ProductUnitDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("productUnitService")
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductUnitService implements ProductUnitPort {        // ← implementa ENTRADA

    private final ProductUnitUseCase productUnitUseCase;     // dominio
    private final ProductUnitRepositoryPort repository;             // salida
    private final ProductUnitDtoMapper      mapper;                 // dto ↔ model

    @Override
    public ProductUnitResponseDto create(ProductUnitCreateDto dto) {

        log.info("Creating ProductUnit with code {}", dto.getCode());

        if (repository.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("Code '" + dto.getCode() + "' already exists");
        }

        ProductUnitModel model = mapper.toModel(dto);
        model.setCreatedAt(LocalDateTime.now());
        model.setCreatedBy(dto.getCreatedBy());

        ProductUnitModel saved = productUnitUseCase.save(model);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductUnitResponseDto> findAll() {
        return productUnitUseCase.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductUnitResponseDto findById(UUID id) {
        ProductUnitModel model = productUnitUseCase.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductUnit not found: " + id));
        return mapper.toResponse(model);
    }

    @Override
    public ProductUnitResponseDto update(UUID id, ProductUnitUpdateDto dto) {
        ProductUnitModel existing = productUnitUseCase.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductUnit not found: " + id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setEnabled(dto.getEnabled());
        existing.setUpdatedBy(dto.getUpdatedBy());
        existing.setUpdatedAt(LocalDateTime.now());

        ProductUnitModel updated = productUnitUseCase.save(existing);
        return mapper.toResponse(updated);
    }

    @Override
    public void delete(UUID id) {
        if (!productUnitUseCase.existsById(id)) {
            throw new RuntimeException("ProductUnit not found: " + id);
        }
        productUnitUseCase.deleteById(id);
    }

    // -------- consultas light --------
    @Override
    public boolean existsByCode(String code) {
        return productUnitUseCase.existsByCode(code);
    }

    @Override
    public Optional<String> findNameByCode(String code) {
        return productUnitUseCase.findNameByCode(code);
    }

    @Override
    public List<String> findAllCodes() {
        return productUnitUseCase.findAllCodes();
    }
}