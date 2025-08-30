package com.celotts.productservice.infrastructure.adapter.input.rest.controller;


import com.celotts.productservice.domain.model.product.ProductBrandModel;
import com.celotts.productservice.domain.port.input.product.ProductBrandUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandDeleteDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productbrand.ProductBrandDtoMapper;
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
@RequestMapping(value = "/api/v1/product-brands", produces = "application/json")
public class ProductBrandController {

    private final ProductBrandUseCase productBrandUseCase;
    private final ProductBrandDtoMapper productBrandDtoMapper;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<ProductBrandResponseDto> create(@Valid @RequestBody ProductBrandCreateDto dto) {
        // Si tu mapper tiene toModel(createDto)
        ProductBrandModel model = productBrandDtoMapper.toModel(dto);
        ProductBrandModel saved = productBrandUseCase.save(model);
        ProductBrandResponseDto response = productBrandDtoMapper.toResponseDto(saved);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/v1/product-brands/" + response.getId())
                .body(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBrands() {
        List<ProductBrandResponseDto> list = productBrandUseCase.findAll()
                .stream()
                .map(productBrandDtoMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(
                Map.of(
                        "data", list,
                        "total", list.size()
                )
        );
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(
            @PathVariable UUID id,
            @Valid @RequestBody ProductBrandDeleteDto dto) {
        productBrandUseCase.deleteById(id, dto.getUpdatedBy(), dto.getUpdatedAt());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ids")
    public List<UUID> getAllBrandIds() {
        return productBrandUseCase.findAllIds();
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
        return ResponseEntity.ok(productBrandDtoMapper.toResponseDto(brand));
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<ProductBrandResponseDto> disableBrand(@PathVariable UUID id) {
        ProductBrandModel brand = productBrandUseCase.disableBrand(id);
        return ResponseEntity.ok(productBrandDtoMapper.toResponseDto(brand));
    }
}