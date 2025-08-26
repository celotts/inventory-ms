package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.product.ProductTagAssignmentModel;
import com.celotts.productservice.domain.port.input.product.ProductTagAssignmentUseCase;

import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTagAssignment.ProductTagAssignmentCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTagAssignment.ProductTagAssignmentUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTagAssignment.ProductTagAssignmentResponseDto;

import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productTagAssignment.ProductTagAssignmentDtoMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product-tag-assignment")
public class ProductTagAssignmentController {

    private final ProductTagAssignmentUseCase productTagAssignmentUseCase;
    private final ProductTagAssignmentDtoMapper productTagAssignmentDtoMapper;

    @PostMapping
    public ResponseEntity<ProductTagAssignmentResponseDto> create(
            @Valid @RequestBody ProductTagAssignmentCreateDto dto) {

        ProductTagAssignmentModel toCreate = productTagAssignmentDtoMapper.toModel(dto);
        ProductTagAssignmentModel created  = productTagAssignmentUseCase.create(toCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/v1/product-tag-assignment/" + created.getId())
                .body(productTagAssignmentDtoMapper.toResponseDto(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductTagAssignmentResponseDto> getById(@PathVariable UUID id) {
        ProductTagAssignmentModel model = productTagAssignmentUseCase.findById(id);
        return ResponseEntity.ok(productTagAssignmentDtoMapper.toResponseDto(model));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductTagAssignmentResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProductTagAssignmentUpdateDto dto) {

        ProductTagAssignmentModel update = productTagAssignmentDtoMapper.toModel(dto);
        ProductTagAssignmentModel saved  = productTagAssignmentUseCase.update(id, update);
        return ResponseEntity.ok(productTagAssignmentDtoMapper.toResponseDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productTagAssignmentUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> list(
            Pageable pageable,
            @RequestParam(required = false) Boolean enabled) {

        if (enabled == null) {
            var page = productTagAssignmentUseCase.findAll(pageable);
            List<ProductTagAssignmentResponseDto> data =
                    page.map(productTagAssignmentDtoMapper::toResponseDto).getContent();
            return ResponseEntity.ok(Map.of(
                    "data", data,
                    "total", page.getTotalElements()
            ));
        } else {
            // cuando filtras por enabled, el puerto retorna List
            List<ProductTagAssignmentResponseDto> data =
                    productTagAssignmentUseCase.findByEnabled(enabled).stream()
                            .map(productTagAssignmentDtoMapper::toResponseDto)
                            .toList();
            return ResponseEntity.ok(Map.of(
                    "data", data,
                    "total", (long) data.size()
            ));
        }
    }

    @GetMapping("/enabled")
    public ResponseEntity<Map<String, Object>> listEnabled() {
        List<ProductTagAssignmentResponseDto> data =
                productTagAssignmentUseCase.findByEnabled(true).stream()
                        .map(productTagAssignmentDtoMapper::toResponseDto)
                        .toList();
        return ResponseEntity.ok(Map.of("data", data, "total", data.size()));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<ProductTagAssignmentResponseDto> enable(@PathVariable UUID id) {
        ProductTagAssignmentModel enabled = productTagAssignmentUseCase.enable(id);
        return ResponseEntity.ok(productTagAssignmentDtoMapper.toResponseDto(enabled));
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<ProductTagAssignmentResponseDto> disable(@PathVariable UUID id) {
        ProductTagAssignmentModel disabled = productTagAssignmentUseCase.disable(id);
        return ResponseEntity.ok(productTagAssignmentDtoMapper.toResponseDto(disabled));
    }

    @GetMapping("/enabled/count")
    public Map<String, Long> countEnabled() {
        return Map.of("count", productTagAssignmentUseCase.countEnabled());
    }
}