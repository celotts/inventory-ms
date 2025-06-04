package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.applications.service.ProductCategoryService;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductCategoryRequestDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductCategoryResponseDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.ProductCategoryRequestMapper;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.ProductCategoryResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product-categories")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService service;
    private final ProductCategoryRequestMapper requestMapper;
    private final ProductCategoryResponseMapper responseMapper;

    @PostMapping
    public ResponseEntity<ProductCategoryResponseDTO> create(@Valid @RequestBody ProductCategoryRequestDTO dto) {
        return ResponseEntity.ok(responseMapper.toDto(service.create(requestMapper.toModel(dto))));
    }

    @GetMapping
    public ResponseEntity<List<ProductCategoryResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll().stream().map(responseMapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(responseMapper.toDto(service.getById(id)));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductCategoryResponseDTO>> getByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(service.getByProductId(productId).stream().map(responseMapper::toDto).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}