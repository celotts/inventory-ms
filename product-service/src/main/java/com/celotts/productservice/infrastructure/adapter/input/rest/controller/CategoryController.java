package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.applications.service.CategoryService;
import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryRequestDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.category.CategoryDtoMapper;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.category.CategoryResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryResponseMapper categoryResponseMapper;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(@Valid @RequestBody CategoryRequestDto categoryCreateDto) {
        var category = CategoryDtoMapper.toModel(categoryCreateDto);
        var createdCategory = categoryService.create(category);
        var responseDto = CategoryDtoMapper.toResponseDto(createdCategory);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryRequestDto categoryUpdateDto) {

        var category = CategoryDtoMapper.toModel(categoryUpdateDto);
        category.setUpdatedBy("system"); // Puedes obtener del contexto de seguridad

        var updatedCategory = categoryService.update(id, category);
        var responseDto = CategoryDtoMapper.toResponseDto(updatedCategory);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> findById(@PathVariable UUID id) {
        return categoryService.findById(id)
                .map(category -> ResponseEntity.ok(CategoryDtoMapper.toResponseDto(category)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> findAll(
            @RequestParam(required = false) String name) {

        List<CategoryModel> categoryModels;

        if (name != null && !name.trim().isEmpty()) {
            categoryModels = categoryService.findByNameContaining(name.trim());
        } else {
            categoryModels = categoryService.findAll();
        }

        List<CategoryResponseDto> categories = CategoryDtoMapper.toResponseDtoList(categoryModels);
        return ResponseEntity.ok(categories);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}