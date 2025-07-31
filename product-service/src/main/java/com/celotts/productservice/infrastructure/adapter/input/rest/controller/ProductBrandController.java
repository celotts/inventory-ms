package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productBrand.ProductBrandDtoMapper;
import com.celotts.productservice.applications.service.ProductBrandService;
import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.domain.port.product.brand.usecase.ProductBrandUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandUpdateDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product-brands")
@CrossOrigin(origins = "${app.cors.allowed-origin:*}")
@Tag(name = "Product Brand API", description = "API para gestionar marcas de productos")
public class ProductBrandController {

    private final ProductBrandService productBrandService;
    private final ProductBrandUseCase productBrandUseCase;
    private final ProductBrandDtoMapper productBrandDtoMapper; // <<--- Añadido aquí

    @PostMapping
    public ResponseEntity<ProductBrandResponseDto> create(@Valid @RequestBody ProductBrandCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productBrandService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProductBrandResponseDto>> getAllBrands() {
        return ResponseEntity.ok(productBrandService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductBrandResponseDto> getBrandById(@PathVariable UUID id) {
        return ResponseEntity.ok(productBrandService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductBrandResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProductBrandUpdateDto dto) {
        return ResponseEntity.ok(productBrandService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productBrandService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/brands/ids")
    public List<UUID> getAllBrandIds() {
        return productBrandService.findAllIds();
    }

    @GetMapping("/brands/{id}/name")
    public String getBrandNameById(@PathVariable UUID id) {
        return productBrandService.findNameById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<ProductBrandResponseDto> enableBrand(@PathVariable UUID id) {
        ProductBrandModel brand = productBrandUseCase.enableBrand(id);
        return ResponseEntity.ok(productBrandDtoMapper.toResponseDto(brand));
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<ProductBrandResponseDto> disableBrand(@PathVariable UUID id) {
        ProductBrandModel brand = productBrandUseCase.disableBrand(id);
        return ResponseEntity.ok(productBrandDtoMapper.toResponseDto(brand));
    }
}