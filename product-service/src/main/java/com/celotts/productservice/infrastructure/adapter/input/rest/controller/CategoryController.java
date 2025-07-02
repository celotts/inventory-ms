package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.domain.port.category.CategoryUseCase;
import com.celotts.productservice.domain.port.product_brand.ProductBrandUseCase;
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

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryUseCase categoryUseCase;
    //private final ProductBrandUseCase productBrandUseCase;

    /**
     * Crear una nueva categoría
     * POST /api/v1/categories
     */
    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(
            @Valid @RequestBody CategoryCreateDto categoryCreateDto) {

        var categoryModel = CategoryDtoMapper.toModelFromCreate(categoryCreateDto);
        var createdCategory = categoryUseCase.save(categoryModel);
        var responseDto = CategoryDtoMapper.toResponseDto(createdCategory);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // Con el mapper estático (Opción B):
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> findById(@PathVariable UUID id) {
        return categoryUseCase.findById(id)
                .map(CategoryResponseMapper::toDto) // ← Uso directo
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualizar una categoría existente
     * PUT /api/v1/categories/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryUpdateDto categoryUpdateDto) {

        var existing = categoryUseCase.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        existing.update(
                categoryUpdateDto.getName(),
                categoryUpdateDto.getDescription(),
                categoryUpdateDto.getActive(),
                categoryUpdateDto.getUpdatedBy()
        );

        var updatedCategory = categoryUseCase.save(existing);

        var responseDto = CategoryDtoMapper.toResponseDto(updatedCategory);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * Obtener todas las categorías con filtros opcionales
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
            categories = categoryUseCase.findByNameContaining(name.trim());
        } else if (active != null) {
            categories = categoryUseCase.findByActive(active);
        } else {
            categories = categoryUseCase.findAll();
        }

        // Aplicar ordenamiento si es necesario
        categories = sortCategories(categories, sortBy, sortDirection);

        List<CategoryResponseDto> responseDtos = CategoryDtoMapper.toResponseDtoList(categories);
        return ResponseEntity.ok(responseDtos);
    }

    private List<CategoryModel> sortCategories(List<CategoryModel> categories, String sortBy, String sortDirection) {
        if (categories == null || categories.isEmpty()) {
            return categories;
        }

        Comparator<CategoryModel> comparator;

        switch (sortBy != null ? sortBy.toLowerCase() : "") {
            case "create" -> comparator = Comparator.comparing(CategoryModel::getCreatedAt);
            case "update" -> comparator = Comparator.comparing(CategoryModel::getUpdatedAt, Comparator.nullsLast(Comparator.naturalOrder()));
            case "active" -> comparator = Comparator.comparing(CategoryModel::getActive, Comparator.nullsLast(Comparator.naturalOrder()));
            case "name", default -> comparator = Comparator.comparing(CategoryModel::getName, String.CASE_INSENSITIVE_ORDER);
        }

        if ("desc".equalsIgnoreCase(sortDirection)) {
            comparator = comparator.reversed();
        }

        return categories.stream()
                .sorted(comparator)
                .toList();
    }

    /**
     * Obtener categorías con paginación
     * GET /api/v1/categories/paginated
     */
    @GetMapping("/paginated")
    public ResponseEntity<Page<CategoryResponseDto>> getCategoriesPaginated(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active,
            Pageable pageable) {

        Page<CategoryModel> categoriesPage = categoryUseCase.findAllPaginated(name, active, pageable);
        Page<CategoryResponseDto> responsePage = categoriesPage.map(CategoryDtoMapper::toResponseDto);

        return ResponseEntity.ok(responsePage);
    }

    /**
     * Obtener categorías activas únicamente
     * GET /api/v1/categories/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<CategoryResponseDto>> getActiveCategories() {
        List<CategoryModel> activeCategories = categoryUseCase.findByActive(true);
        List<CategoryResponseDto> responseDtos = CategoryDtoMapper.toResponseDtoList(activeCategories);
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * Buscar categorías por nombre (búsqueda más flexible)
     * GET /api/v1/categories/search
     */
    @GetMapping("/search")
    public ResponseEntity<List<CategoryResponseDto>> searchCategories(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {

        List<CategoryModel> categories = categoryUseCase.searchByNameOrDescription(query, limit);
        List<CategoryResponseDto> responseDtos = CategoryDtoMapper.toResponseDtoList(categories);
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * Activar/Desactivar una categoría
     * PATCH /api/v1/categories/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<CategoryResponseDto> toggleCategoryStatus(
            @PathVariable UUID id,
            @RequestParam Boolean active) {

        var updatedCategory = categoryUseCase.updateStatus(id, active);
        var responseDto = CategoryDtoMapper.toResponseDto(updatedCategory);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * Eliminar una categoría (soft delete)
     * DELETE /api/v1/categories/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Eliminar permanentemente una categoría
     * DELETE /api/v1/categories/{id}/permanent
     */
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Void> permanentDeleteCategory(@PathVariable UUID id) {
        categoryUseCase.permanentDelete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaurar una categoría eliminada
     * PATCH /api/v1/categories/{id}/restore
     */
    @PatchMapping("/{id}/restore")
    public ResponseEntity<CategoryResponseDto> restoreCategory(@PathVariable UUID id) {
        var restoredCategory = categoryUseCase.restore(id);
        var responseDto = CategoryDtoMapper.toResponseDto(restoredCategory);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Obtener estadísticas de categorías
     * GET /api/v1/categories/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<CategoryStatsDto> getCategoryStats() {
        var stats = categoryUseCase.getCategoryStatistics();
        return ResponseEntity.ok(stats);
    }

    /**
     * Validar si existe una categoría con el nombre dado
     * GET /api/v1/categories/exists
     */
    @GetMapping("/exists")
    public ResponseEntity<Boolean> categoryExists(@RequestParam String name) {
        boolean exists = categoryUseCase.existsByName(name);
        return ResponseEntity.ok(exists);
    }

    @DeleteMapping("/category")
    public ResponseEntity<Void> deleteCategory(@RequestBody @Valid CategoryDeleteDto dto) {
        categoryUseCase.deleteById(dto.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByName(@RequestParam String name) {
        //TODO: ERROR
        boolean exists = categoryUseCase.existsByName(name);
        return ResponseEntity.ok(exists);
    }

}
