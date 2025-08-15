package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.domain.port.input.category.CategoryUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.category.CategoryDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryUseCase categoryUseCase;
    private final CategoryDtoMapper categoryDtoMapper;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(@Valid @RequestBody CategoryCreateDto dto) {
        CategoryModel model = categoryDtoMapper.toModel(dto);
        CategoryModel saved = categoryUseCase.save(model);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/v1/categories/" + saved.getId())
                .body(categoryDtoMapper.toResponseDto(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getById(@PathVariable UUID id) {
        CategoryModel model = categoryUseCase.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found: " + id));
        return ResponseEntity.ok(categoryDtoMapper.toResponseDto(model));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> update(@PathVariable UUID id,
                                                      @Valid @RequestBody CategoryUpdateDto dto) {
        CategoryModel current = categoryUseCase.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found: " + id));
        CategoryModel merged = categoryDtoMapper.updateModelFromDto(current, dto);
        CategoryModel saved = categoryUseCase.save(merged);
        return ResponseEntity.ok(categoryDtoMapper.toResponseDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> list(Pageable pageable,
                                                    @RequestParam(required = false) String term) {
        var page = (term == null || term.isBlank())
                ? categoryUseCase.findAll(pageable)
                : categoryUseCase.searchByNameOrDescription(term, pageable);

        var data = page.map(categoryDtoMapper::toResponseDto).getContent();
        return ResponseEntity.ok(Map.of("data", data, "total", page.getTotalElements()));
    }
}