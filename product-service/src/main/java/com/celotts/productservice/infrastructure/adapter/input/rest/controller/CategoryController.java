package com.celotts.productservice.infrastructure.adapter.input.rest.controller;


import com.celotts.productservice.domain.model.CategoryModel;

import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.*;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.*;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.category.CategoryDtoMapper;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.category.CategoryResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "${app.cors.allowed-origin:*}")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Crear una nueva categoría.
     * POST /api/v1/categories
     */
    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(
            @Valid @RequestBody CategoryCreateDto categoryCreateDto) {

        var categoryModel = CategoryDtoMapper.toModelFromCreate(categoryCreateDto);
        var createdCategory = categoryService.create(categoryModel);
        var responseDto = CategoryDtoMapper.toResponseDto(createdCategory);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * Obtener una categoría por ID.
     * GET /api/v1/categories/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> findById(@PathVariable UUID id) {
        return categoryService.findById(id)
                .map(CategoryResponseMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualizar una categoría existente.
     * PUT /api/v1/categories/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryUpdateDto categoryUpdateDto) {

        var dto = CategoryDtoMapper.toModelFromUpdate(categoryUpdateDto);
        var updatedCategory = categoryService.update(id, dto);

        var responseDto = CategoryDtoMapper.toResponseDto(updatedCategory);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * Obtener todas las categorías con filtros opcionales.
     * GET /api/v1/categories
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        List<CategoryModel> categories;

        if (name != null && !name.trim().isEmpty()) {
            categories = categoryService.findAll().stream()
                .filter(c -> c.getName() != null && c.getName().toLowerCase().contains(name.trim().toLowerCase()))
                .toList();
        } else if (active != null) {
            categories = categoryService.findAll().stream()
                .filter(c -> Objects.equals(c.getActive(), active))
                .toList();
        } else {
            categories = categoryService.findAll();
        }

        // Aplicar ordenamiento si es necesario.
        categories = sortCategories(categories, sortBy, sortDirection);

        List<CategoryResponseDto> responseDtos = CategoryDtoMapper.toResponseDtoList(categories);
        return ResponseEntity.ok(responseDtos);
    }

    private static List<CategoryModel> sortCategories(List<CategoryModel> categories, String sortBy, String sortDirection) {
        if (categories == null || categories.isEmpty()) {
            return categories;
        }

        Comparator<CategoryModel> comparator;

        switch (sortBy != null ? sortBy.toLowerCase() : "") {
            case "create" -> comparator = Comparator.comparing(CategoryModel::getCreatedAt);
            case "update" -> comparator = Comparator.comparing(CategoryModel::getUpdatedAt, Comparator.nullsLast(Comparator.naturalOrder()));
            case "active" -> comparator = Comparator.comparing(CategoryModel::getActive, Comparator.nullsLast(Comparator.naturalOrder()));
            case "name" -> comparator = Comparator.comparing(CategoryModel::getName, String.CASE_INSENSITIVE_ORDER);
            default -> throw new IllegalArgumentException("Unsupported sortBy value: " + sortBy);
        }

        if ("desc".equalsIgnoreCase(sortDirection)) {
            comparator = comparator.reversed();
        }

        return categories.stream()
                .sorted(comparator)
                .toList();
    }

    /**
     * Obtener categorías con paginación.
     * GET /api/v1/categories/paginated
     */
    @GetMapping("/paginated")
    public ResponseEntity<Page<CategoryResponseDto>> getPaginatedCategories(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active,
            Pageable pageable) {

        Page<CategoryModel> categoriesPage = categoryService.findAllPaginated(name, active, pageable);
        Page<CategoryResponseDto> responsePage = categoriesPage.map(CategoryDtoMapper::toResponseDto);

        return ResponseEntity.ok(responsePage);
    }

    /**
     * Obtener categorías activas únicamente.
     * GET /api/v1/categories/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<CategoryResponseDto>> getActiveCategories() {
        List<CategoryModel> activeCategories = categoryService.findAll().stream()
            .filter(c -> Boolean.TRUE.equals(c.getActive()))
            .toList();
        List<CategoryResponseDto> responseDtos = CategoryDtoMapper.toResponseDtoList(activeCategories);
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * Buscar categorías por nombre (búsqueda más flexible).
     * GET /api/v1/categories/search
     */
    @GetMapping("/search")
    public ResponseEntity<List<CategoryResponseDto>> searchCategories(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {

        List<CategoryModel> categories = categoryService.searchByNameOrDescription(query, limit);
        List<CategoryResponseDto> responseDtos = CategoryDtoMapper.toResponseDtoList(categories);
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * Activar/Desactivar una categoría.
     * PATCH /api/v1/categories/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<CategoryResponseDto> toggleCategoryStatus(
            @PathVariable UUID id,
            @RequestParam Boolean active) {

        var updatedCategory = categoryService.updateStatus(id, active);
        if (updatedCategory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        var responseDto = CategoryDtoMapper.toResponseDto(updatedCategory);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * Eliminar permanentemente una categoría.
     * DELETE /api/v1/categories/{id}/permanent
     */
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Void> permanentDeleteCategory(@PathVariable UUID id) {
        categoryService.permanentDelete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCategory(@Valid @RequestBody CategoryDeleteDto deleteDto) {
        categoryService.permanentDelete(deleteDto.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaurar una categoría eliminada.
     * PATCH /api/v1/categories/{id}/restore
     */
    @PatchMapping("/{id}/restore")
    public ResponseEntity<CategoryResponseDto> restoreCategory(@PathVariable UUID id) {
        var restoredCategory = categoryService.restore(id);
        var responseDto = CategoryDtoMapper.toResponseDto(restoredCategory);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Obtener estadísticas de categorías.
     * GET /api/v1/categories/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<CategoryStatusDto> getCategoryStats() {
        var stats = categoryService.getCategoryStatistics();
        return ResponseEntity.ok(stats);
    }

    /**
     * Verifica si existe una categoría con el nombre dado.
     */
    @GetMapping("/exists")
    public ResponseEntity<Boolean> categoryExists(@RequestParam String name) {
        boolean exists = categoryService.existsByName(name);
        return ResponseEntity.ok(exists);
    }



}
