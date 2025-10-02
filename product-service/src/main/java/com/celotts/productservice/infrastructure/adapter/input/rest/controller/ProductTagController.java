package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.product.ProductTagModel;
import com.celotts.productservice.domain.port.input.product.ProductTagUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttag.ProductTagCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttag.ProductTagResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttag.ProductTagUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.response.ListResponse;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.response.PageResponse;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.producttag.ProductTagMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
    public ResponseEntity<?> list(Pageable pageable, @RequestParam(required = false) String name) {
        if (name == null || name.isBlank()) {
            var page = productTagUseCase.findAll(pageable);
            return ResponseEntity.ok(PageResponse.from(page, mapper::toResponse));
        } else {
            return productTagUseCase.findByName(name)
                    .map(tag -> ResponseEntity.ok(ListResponse.of(List.of(mapper.toResponse(tag)))))
                    .orElseGet(() -> ResponseEntity.ok(ListResponse.of(List.of())));
        }
    }


    @GetMapping("/enabled")
    public ResponseEntity<ListResponse<ProductTagResponseDto>> listEnabled() {
        List<ProductTagResponseDto> data = productTagUseCase.findAllEnabled()
                .stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(ListResponse.of(data));
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