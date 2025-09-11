package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.port.input.category.CategoryUseCase;
import com.celotts.productservice.domain.model.category.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.category.CategoryMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryUseCase categoryUseCase;
    private final CategoryMapper categoryMapper;     // 👈 inyecta el MapStruct mapper

    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(@Valid @RequestBody CategoryCreateDto dto) {
        CategoryModel model = categoryMapper.toModel(dto);                 // ✅ MapStruct
        CategoryModel saved = categoryUseCase.save(model);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryMapper.toResponse(saved));
    }


    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getById(@PathVariable UUID id) {
        CategoryModel model = categoryUseCase.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Category not found: " + id));
        return ResponseEntity.ok(categoryMapper.toResponse(model)); // ✅ MapStruct
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> update(@PathVariable UUID id,
                                                      @Valid @RequestBody CategoryUpdateDto dto) {
        CategoryModel existing = categoryUseCase.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Category not found: " + id));

        // ✅ Update parcial con MapStruct (ignora nulls)
        categoryMapper.updateModelFromDto(existing, dto);

        CategoryModel saved = categoryUseCase.save(existing);
        return ResponseEntity.ok(categoryMapper.toResponse(saved));        // ✅ MapStruct
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponseDto>> list(Pageable pageable) {
        Page<CategoryResponseDto> out = categoryMapper.toResponsePage(categoryUseCase.findAll(pageable));
        return ResponseEntity.ok(out);
    }
}