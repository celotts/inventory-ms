package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.product.ProductBrandModel;
import com.celotts.productservice.domain.port.input.product.ProductBrandUseCase;

import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandDeleteDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productBrand.ProductBrandMapper;
import com.celotts.productservice.domain.exception.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/product-brands", produces = "application/json")
public class ProductBrandController {

    private final ProductBrandUseCase productBrandUseCase;
    private final ProductBrandMapper productBrandMapper;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<ProductBrandResponseDto> create(@Valid @RequestBody ProductBrandCreateDto dto) {
        ProductBrandModel model = productBrandMapper.toModel(dto);
        ProductBrandModel saved = productBrandUseCase.save(model);
        ProductBrandResponseDto response = productBrandMapper.toResponse(saved);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/v1/product-brands/" + response.getId())
                .body(response);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<ProductBrandResponseDto> update(@PathVariable UUID id,
                                                          @Valid @RequestBody ProductBrandUpdateDto dto) {
        log.info("PATCH brand id={} payload={}", id, dto);   // <— inspecciona entrada

        ProductBrandModel patch = ProductBrandModel.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .enabled(dto.getEnabled())
                .updatedBy(dto.getUpdatedBy())
                .build();

        ProductBrandModel updated = productBrandUseCase.update(id, patch);
        return ResponseEntity.ok(productBrandMapper.toResponse(updated));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBrands() {
        List<ProductBrandResponseDto> list = productBrandUseCase.findAll()
                .stream()
                .map(productBrandMapper::toResponse)
                .toList();

        return ResponseEntity.ok(Map.of("data", list, "total", list.size()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(
            @PathVariable UUID id,
            @Valid @RequestBody ProductBrandDeleteDto dto) {

        productBrandUseCase.deleteById(id, dto.getDeletedBy(), dto.getReason());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductBrandResponseDto> getById(@PathVariable UUID id) {
        return productBrandUseCase.findById(id)
                .map(productBrandMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Brand with id " + id + " not found"));
    }

    @GetMapping("/{id}/name")
    public ResponseEntity<String> getBrandNameById(@PathVariable UUID id) {
        return productBrandUseCase.findNameById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand name not found"));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<ProductBrandResponseDto> enableBrand(@PathVariable UUID id) {
        ProductBrandModel brand = productBrandUseCase.enableBrand(id);
        return ResponseEntity.ok(productBrandMapper.toResponse(brand));
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<ProductBrandResponseDto> disableBrand(@PathVariable UUID id) {
        ProductBrandModel brand = productBrandUseCase.disableBrand(id);
        return ResponseEntity.ok(productBrandMapper.toResponse(brand));
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<ProductBrandResponseDto> replace(@PathVariable UUID id,
                                                           @Valid @RequestBody ProductBrandUpdateDto dto) {
        // Si quieres exigir todos los campos en PUT, valida aquí.
        ProductBrandModel patch = ProductBrandModel.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .enabled(dto.getEnabled())
                .updatedBy(dto.getUpdatedBy())
                .build();
        ProductBrandModel updated = productBrandUseCase.update(id, patch);
        return ResponseEntity.ok(productBrandMapper.toResponse(updated));
    }
}