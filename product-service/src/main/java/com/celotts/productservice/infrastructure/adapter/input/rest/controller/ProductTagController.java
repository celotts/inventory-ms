package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.product.ProductTagModel;
import com.celotts.productservice.domain.port.input.product.ProductTagUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTag.ProductTagCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTag.ProductTagUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTag.ProductTagResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productTag.ProductTagMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product-tags")
@CrossOrigin(origins = "${app.cors.allowed-origin:*}")
@Slf4j
public class ProductTagController {

    private final ProductTagUseCase productTagUseCase;
    private final ProductTagMapper mapper;

    @PostMapping
    public ResponseEntity<ProductTagResponseDto> create(@Valid @RequestBody ProductTagCreateDto dto) {
        ProductTagModel toCreate = mapper.toModel(dto);                  // <- mapper (no productTagMapper)
        ProductTagModel created  = productTagUseCase.create(toCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/v1/product-tags/" + created.getId())
                .body(mapper.toResponse(created));                       // <- toResponse (no toResponseDto)
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductTagResponseDto> update(@PathVariable UUID id, @Valid @RequestBody ProductTagUpdateDto dto) {
        ProductTagModel existing = productTagUseCase.findById(id);
        mapper.updateModelFromDto(existing, dto);              // patch ignorando nulls
        ProductTagModel saved = productTagUseCase.update(id, existing); // o save(existing) según tu puerto
        return ResponseEntity.ok(mapper.toResponse(saved));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productTagUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> list(Pageable pageable, @RequestParam(required = false) String name) {
        if (name == null || name.isBlank()) {
            var page = productTagUseCase.findAll(pageable);
            var data = page.map(mapper::toResponse).getContent();
            return ResponseEntity.ok(Map.of("data", data, "total", page.getTotalElements()));
        } else {
            return productTagUseCase.findByName(name)
                    .map(tag -> ResponseEntity.ok(Map.<String,Object>of("data", List.of(mapper.toResponse(tag)), "total", 1L)))
                    .orElseGet(() -> ResponseEntity.ok(Map.<String,Object>of("data", List.of(), "total", 0L)));
        }
    }

    @GetMapping("/enabled")
    public ResponseEntity<Map<String, Object>> listEnabled() {
        List<ProductTagResponseDto> data = productTagUseCase.findAllEnabled()
                .stream()
                .map(mapper::toResponse)                                  // <- toResponse
                .toList();
        return ResponseEntity.ok(Map.of("data", data, "total", data.size()));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<ProductTagResponseDto> enable(@PathVariable UUID id) {
        ProductTagModel enabled = productTagUseCase.enable(id);
        return ResponseEntity.ok(mapper.toResponse(enabled));
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<ProductTagResponseDto> disable(@PathVariable UUID id) {
        ProductTagModel disabled = productTagUseCase.disable(id);
        return ResponseEntity.ok(mapper.toResponse(disabled));
    }

    @GetMapping("/enabled/count")
    public Map<String, Long> countEnabled() {
        return Map.of("count", productTagUseCase.countEnabled());
    }        // ← use case tiene countEnabled()

}