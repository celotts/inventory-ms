package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.ProductTagModel;
import com.celotts.productservice.domain.port.input.product.ProductTagUseCase;

import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductTag.ProductTagCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductTag.ProductTagResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductTag.ProductTagUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productTag.ProductTagDtoMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product-tags")
public class ProductTagController {

    private final ProductTagUseCase productTagUseCase;
    private final ProductTagDtoMapper productTagDtoMapper;

    @PostMapping
    public ResponseEntity<ProductTagResponseDto> create(@Valid @RequestBody ProductTagCreateDto dto) {
        ProductTagModel toCreate = productTagDtoMapper.toModel(dto);
        ProductTagModel created  = productTagUseCase.create(toCreate);        // ← use case tiene create(...)
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/v1/product-tags/" + created.getId())
                .body(productTagDtoMapper.toResponseDto(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductTagResponseDto> getById(@PathVariable UUID id) {
        // El puerto define findById(UUID) -> ProductTagModel (no Optional)
        ProductTagModel model = productTagUseCase.findById(id);
        return ResponseEntity.ok(productTagDtoMapper.toResponseDto(model));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductTagResponseDto> update(@PathVariable UUID id,
                                                        @Valid @RequestBody ProductTagUpdateDto dto) {
        ProductTagModel update = productTagDtoMapper.toModel(dto);            // mapea campos editables
        ProductTagModel saved  = productTagUseCase.update(id, update);        // ← use case tiene update(id, model)
        return ResponseEntity.ok(productTagDtoMapper.toResponseDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productTagUseCase.delete(id);                                         // ← use case tiene delete(UUID)
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> list(Pageable pageable,
                                                    @RequestParam(required = false) String name) {
        if (name == null || name.isBlank()) {
            var page = productTagUseCase.findAll(pageable);                   // ← Page<ProductTagModel>
            List<ProductTagResponseDto> data = page.map(productTagDtoMapper::toResponseDto).getContent();
            return ResponseEntity.ok(Map.of("data", data, "total", page.getTotalElements()));
        } else {
            // El puerto SOLO tiene findByName(String) -> Optional<ProductTagModel>
            return productTagUseCase.findByName(name)
                    .map(tag -> ResponseEntity.ok(Map.of("data", List.of(productTagDtoMapper.toResponseDto(tag)),
                            "total", 1)))
                    .orElse(ResponseEntity.ok(Map.of("data", List.of(), "total", 0)));
        }
    }

    @GetMapping("/enabled")
    public ResponseEntity<Map<String, Object>> listEnabled() {
        List<ProductTagResponseDto> data = productTagUseCase.findAllEnabled().stream()
                .map(productTagDtoMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(Map.of("data", data, "total", data.size()));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<ProductTagResponseDto> enable(@PathVariable UUID id) {
        ProductTagModel enabled = productTagUseCase.enable(id);               // ← use case tiene enable(UUID)
        return ResponseEntity.ok(productTagDtoMapper.toResponseDto(enabled));
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<ProductTagResponseDto> disable(@PathVariable UUID id) {
        ProductTagModel disabled = productTagUseCase.disable(id);             // ← use case tiene disable(UUID)
        return ResponseEntity.ok(productTagDtoMapper.toResponseDto(disabled));
    }

    @GetMapping("/enabled/count")
    public Map<String, Long> countEnabled() {
        return Map.of("count", productTagUseCase.countEnabled());             // ← use case tiene countEnabled()
    }
}