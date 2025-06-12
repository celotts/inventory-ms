package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.applications.service.ProductBrandService;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product-brands")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductBrandController {

    private final ProductBrandService productBrandService;

    @PostMapping
    public ResponseEntity<ProductBrandResponseDto> create(@Valid @RequestBody ProductBrandCreateDto createDto) {
        ProductBrandResponseDto response = productBrandService.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductBrandResponseDto>> getAllBrands() {
        List<ProductBrandResponseDto> brands = productBrandService.findAll();
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductBrandResponseDto> getBrandById(@PathVariable UUID id) {
        ProductBrandResponseDto brand = productBrandService.findById(id);
        return ResponseEntity.ok(brand);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductBrandResponseDto> updateBrand(
            @PathVariable UUID id,
            @Valid @RequestBody ProductBrandCreateDto updateDto) {
        ProductBrandResponseDto response = productBrandService.update(id, updateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable UUID id) {
        productBrandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}