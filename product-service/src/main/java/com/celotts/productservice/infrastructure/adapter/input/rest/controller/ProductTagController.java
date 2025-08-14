package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.ProductTagModel;
import com.celotts.productservice.domain.port.input.product.ProductTagUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTag.*;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductTag.ProductTagCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductTag.ProductTagResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductTag.ProductTagUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productTag.ProductTagRequestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class ProductTagController {

    private final ProductTagUseCase useCase;
    private final ProductTagRequestMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductTagResponseDto create(@Valid @RequestBody ProductTagCreateDto dto) {
        ProductTagModel saved = useCase.create(mapper.toModel(dto));
        return mapper.toResponse(saved);
    }

    @PutMapping("/{id}")
    public ProductTagResponseDto update(@PathVariable UUID id, @Valid @RequestBody ProductTagUpdateDto dto) {
        ProductTagModel saved = useCase.update(id, mapper.toModel(dto));
        return mapper.toResponse(saved);
    }

    @GetMapping("/{id}")
    public ProductTagResponseDto get(@PathVariable UUID id) {
        return mapper.toResponse(useCase.findById(id));
    }

    @GetMapping
    public Page<ProductTagResponseDto> list(Pageable pageable) {
        return useCase.findAll(pageable).map(mapper::toResponse);
    }

    @GetMapping("/enabled")
    public java.util.List<ProductTagResponseDto> enabled() {
        return useCase.findAllEnabled().stream().map(mapper::toResponse).toList();
    }

    @PatchMapping("/{id}/enable")
    public ProductTagResponseDto enable(@PathVariable UUID id) {
        return mapper.toResponse(useCase.enable(id));
    }

    @PatchMapping("/{id}/disable")
    public ProductTagResponseDto disable(@PathVariable UUID id) {
        return mapper.toResponse(useCase.disable(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) { useCase.delete(id); }
}