package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.category.CategoryModel;
import com.celotts.productservice.domain.port.input.category.CategoryUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.response.ApiResponse;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.response.PageResponse;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.category.CategoryMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryUseCase categoryUseCase;
    private final CategoryMapper categoryMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponseDto>> create(@Valid @RequestBody CategoryCreateDto dto) {
        CategoryModel model = categoryMapper.toModel(dto);                 // ✅ MapStruct
        CategoryModel saved = categoryUseCase.save(model);
        CategoryResponseDto out = categoryMapper.toResponse(saved);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(out));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> getById(@PathVariable UUID id) {
        CategoryModel model = categoryUseCase.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found: " + id));
        // 200 + { status, message, timestamp, data }
        return ResponseEntity.ok(ApiResponse.ok(categoryMapper.toResponse(model)));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<CategoryResponseDto>> update(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryUpdateDto dto) {

        CategoryModel existing = categoryUseCase.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found: " + id));

        categoryMapper.updateModelFromDto(existing, dto);
        CategoryModel saved = categoryUseCase.save(existing);

        return ResponseEntity.ok(ApiResponse.ok(categoryMapper.toResponse(saved)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryUseCase.deleteById(id);
        return ResponseEntity.noContent().build(); // ← (3) Mantén 204 (opción recomendada)
        // Si prefieres JSON consistente:
        // return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CategoryResponseDto>>> list(Pageable pageable) {
        Page<CategoryResponseDto> page = categoryMapper.toResponsePage(categoryUseCase.findAll(pageable));
        return ResponseEntity.ok(ApiResponse.ok(PageResponse.from(page)));
    }
}