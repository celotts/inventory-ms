package com.celotts.productserviceOld.infrastructure.adapter.input.rest.controller;

import com.celotts.productserviceOld.infrastructure.adapter.input.rest.mapper.productBrand.ProductBrandDtoMapper;
import com.celotts.productserviceOld.applications.service.ProductBrandService;
import com.celotts.productserviceOld.domain.model.ProductBrandModel;
import com.celotts.productserviceOld.domain.port.product.brand.usecase.ProductBrandUseCase;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandCreateDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandUpdateDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product-brands")
public class ProductBrandController {

    private final ProductBrandUseCase productBrandUseCase;
    private final ProductBrandDtoMapper productBrandDtoMapper;
    private final ProductBrandService productBrandService;

    @PostMapping
    public ResponseEntity<ProductBrandResponseDto> create(@Valid @RequestBody ProductBrandCreateDto dto) {
        System.out.println(">>> [DEBUG] Creating brand: " + dto);
        ProductBrandResponseDto response = productBrandService.create(dto);
        System.out.println(">>> [DEBUG] Created brand ID: " + response.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/v1/product-brands/" + response.getId())
                .body(response);
    }

    /*@GetMapping
    public ResponseEntity<Map<String, Object>> getAllBrands() {
        List<ProductBrandResponseDto> brands = productBrandService.findAll();
        return ResponseEntity.ok(
            Map.of(
                "data", brands,
                "total", brands != null ? brands.size() : 0
            )
        );
    }*/

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBrands() {
        List<ProductBrandResponseDto> brands = productBrandService.findAll();
        int total = (brands == null) ? 0 : brands.size();
        List<ProductBrandResponseDto> safe = (brands == null) ? java.util.List.of() : brands;

        return ResponseEntity.ok(
                java.util.Map.of(
                        "data", safe,   // nunca null
                        "total", total
                )
        );
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
    public ResponseEntity<String> getBrandNameById(@PathVariable UUID id) {
        return productBrandService.findNameById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand name not found"));
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