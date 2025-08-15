package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.domain.port.input.product.ProductBrandUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productBrand.ProductBrandDtoMapper;
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

    @PostMapping
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

    //TODO: HAY ERROR
    /*@GetMapping("/{id}")
    public ResponseEntity<ProductBrandResponseDto> getBrandById(@PathVariable UUID id) {
        ProductBrandModel model = productBrandUseCase.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand not found: " + id));
        return ResponseEntity.ok(productBrandDtoMapper.toResponseDto(model));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductBrandResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProductBrandUpdateDto dto
    ) {
        ProductBrandModel current = productBrandUseCase.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand not found: " + id));

        // Actualiza el modelo con el DTO (según tus métodos del mapper)
        ProductBrandModel merged = productBrandDtoMapper.updateModelFromDto(current, dto);
        ProductBrandModel saved = productBrandUseCase.save(merged);

        return ResponseEntity.ok(productBrandDtoMapper.toResponseDto(saved));
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productBrandUseCase.deleteById(id);
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