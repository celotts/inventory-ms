package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.category.CategoryModel;
import com.celotts.productservice.domain.port.input.category.CategoryUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.category.CategoryDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static com.celotts.productservice.infrastructure.adapter.input.rest.mapper.category.CategoryDtoMapper.toResponseDto;

import java.util.UUID;



@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryUseCase categoryUseCase;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(@Valid @RequestBody CategoryCreateDto dto) {
        CategoryModel model = CategoryDtoMapper.toModelFromCreate(dto);
        CategoryModel saved = categoryUseCase.save(model);
        return ResponseEntity.ok(toResponseDto(saved));
    }


    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getById(@PathVariable UUID id) {
        CategoryModel model = categoryUseCase.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Category not found: " + id));
        return ResponseEntity.ok(toResponseDto(model)); // <- estático
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> update(@PathVariable UUID id,
                                                      @Valid @RequestBody CategoryUpdateDto dto) {
        return categoryUseCase.findById(id)
                .map(existing -> {
                    CategoryModel patch = CategoryDtoMapper.toModelFromUpdate(dto);
                    if (patch.getName() != null)        existing.setName(patch.getName());
                    if (patch.getDescription() != null) existing.setDescription(patch.getDescription());
                    if (patch.getActive() != null)      existing.setActive(patch.getActive());
                    existing.setUpdatedBy(patch.getUpdatedBy());
                    existing.setUpdatedAt(patch.getUpdatedAt());

                    CategoryModel saved = categoryUseCase.save(existing);
                    return ResponseEntity.ok(toResponseDto(saved));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponseDto>> list(Pageable pageable) {
        Page<CategoryModel> page = categoryUseCase.findAll(pageable);
        Page<CategoryResponseDto> out = page.map(CategoryDtoMapper::toResponseDto); // método estático
        return ResponseEntity.ok(out);
    }
}