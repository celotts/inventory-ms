package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.applications.service.CategoryService;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.CategoryRequestDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.CategoryResponseDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.CategoryRequestMapper;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.CategoryResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;
    private final CategoryRequestMapper requestMapper;
    private final CategoryResponseMapper responseMapper;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@Valid @RequestBody CategoryRequestDTO dto) {
        return ResponseEntity.ok(responseMapper.toDto(service.create(requestMapper.toModel(dto))));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll().stream().map(responseMapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(responseMapper.toDto(service.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody CategoryRequestDTO dto) {
        return ResponseEntity.ok(responseMapper.toDto(service.update(id, requestMapper.toModel(dto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}