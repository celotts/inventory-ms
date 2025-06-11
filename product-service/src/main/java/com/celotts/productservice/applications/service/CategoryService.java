package com.celotts.productservice.applications.service;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.domain.port.category.CategoryRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryStatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepositoryPort categoryRepositoryPort;

    // ========== MÉTODOS EXISTENTES (mantenidos) ==========

    public CategoryModel create(CategoryModel category) {
        if (categoryRepositoryPort.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category with name '" + category.getName() + "' already exists");
        }

        category.setCreatedAt(LocalDateTime.now());
        return categoryRepositoryPort.save(category);
    }

    public CategoryModel update(UUID id, CategoryModel category) {
        Optional<CategoryModel> existingCategory = categoryRepositoryPort.findById(id);
        if (existingCategory.isEmpty()) {
            throw new IllegalArgumentException("Category with ID " + id + " not found");
        }

        CategoryModel existing = existingCategory.get();
        existing.update(
                category.getName(),
                category.getDescription(),
                category.getActive(),
                category.getUpdatedBy()
        );

        return categoryRepositoryPort.save(existing);
    }

    @Transactional(readOnly = true)
    public Optional<CategoryModel> findById(UUID id) {
        return categoryRepositoryPort.findById(id);
    }

    @Transactional(readOnly = true)
    public List<CategoryModel> findAll() {
        return categoryRepositoryPort.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<CategoryModel> findByName(String name) {
        return categoryRepositoryPort.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<CategoryModel> findByNameContaining(String name) {
        return categoryRepositoryPort.findByNameContaining(name);
    }

    public void deleteById(UUID id) {
        if (!categoryRepositoryPort.existsById(id)) {
            throw new IllegalArgumentException("Category with ID " + id + " not found");
        }
        categoryRepositoryPort.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return categoryRepositoryPort.existsById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return categoryRepositoryPort.existsByName(name);
    }

    // ========== MÉTODOS NUEVOS AGREGADOS ==========

    /**
     * Buscar categorías por estado activo/inactivo
     */
    @Transactional(readOnly = true)
    public List<CategoryModel> findByActive(Boolean active) {
        return categoryRepositoryPort.findByActive(active);
    }

    /**
     * Buscar categorías con paginación y filtros
     */
    @Transactional(readOnly = true)
    public Page<CategoryModel> findAllPaginated(String name, Boolean active, Pageable pageable) {
        if (name != null && !name.trim().isEmpty() && active != null) {
            // Buscar por nombre y estado
            return categoryRepositoryPort.findByNameContainingAndActive(name.trim(), active, pageable);
        } else if (name != null && !name.trim().isEmpty()) {
            // Solo por nombre
            return categoryRepositoryPort.findByNameContaining(name.trim(), pageable);
        } else if (active != null) {
            // Solo por estado
            return categoryRepositoryPort.findByActive(active, pageable);
        } else {
            // Sin filtros
            return categoryRepositoryPort.findAll(pageable);
        }
    }

    /**
     * Ordenar categorías por campo y dirección
     */
    @Transactional(readOnly = true)
    public List<CategoryModel> sortCategories(List<CategoryModel> categories, String sortBy, String sortDirection) {
        if (categories == null || categories.isEmpty()) {
            return categories;
        }

        Comparator<CategoryModel> comparator;

        switch (sortBy.toLowerCase()) {
            case "name":
                comparator = Comparator.comparing(CategoryModel::getName, String.CASE_INSENSITIVE_ORDER);
                break;
            case "createdat":
                comparator = Comparator.comparing(CategoryModel::getCreatedAt);
                break;
            case "updatedat":
                comparator = Comparator.comparing(CategoryModel::getUpdatedAt,
                        Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "active":
                comparator = Comparator.comparing(CategoryModel::getActive,
                        Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            default:
                comparator = Comparator.comparing(CategoryModel::getName, String.CASE_INSENSITIVE_ORDER);
        }

        if ("desc".equalsIgnoreCase(sortDirection)) {
            comparator = comparator.reversed();
        }

        return categories.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    /**
     * Buscar por nombre o descripción con límite
     */
    @Transactional(readOnly = true)
    public List<CategoryModel> searchByNameOrDescription(String query, int limit) {
        if (query == null || query.trim().isEmpty()) {
            return findAll().stream()
                    .limit(limit)
                    .collect(Collectors.toList());
        }

        String searchTerm = query.trim().toLowerCase();

        return categoryRepositoryPort.findAll().stream()
                .filter(category ->
                        (category.getName() != null && category.getName().toLowerCase().contains(searchTerm)) ||
                                (category.getDescription() != null && category.getDescription().toLowerCase().contains(searchTerm))
                )
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Actualizar solo el estado (activo/inactivo)
     */
    public CategoryModel updateStatus(UUID id, Boolean active) {
        Optional<CategoryModel> existingCategory = categoryRepositoryPort.findById(id);
        if (existingCategory.isEmpty()) {
            throw new IllegalArgumentException("Category with ID " + id + " not found");
        }

        CategoryModel existing = existingCategory.get();
        existing.setActive(active);
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setUpdatedBy("system"); // Esto debería venir del contexto de seguridad

        return categoryRepositoryPort.save(existing);
    }

    /**
     * Eliminación permanente (hard delete)
     * Nota: Solo usar si el repository soporta eliminación física
     */
    public void permanentDelete(UUID id) {
        if (!categoryRepositoryPort.existsById(id)) {
            throw new IllegalArgumentException("Category with ID " + id + " not found");
        }

        // Si implementas soft delete, aquí llamarías a un método específico
        // Por ahora, usa el delete normal
        categoryRepositoryPort.deleteById(id);
    }

    /**
     * Restaurar categoría eliminada
     * Nota: Solo aplicable si implementas soft delete
     */
    public CategoryModel restore(UUID id) {
        // Implementación depende de cómo manejes el soft delete
        // Por ejemplo, si tienes un campo "deleted" en tu modelo:

        Optional<CategoryModel> categoryOpt = categoryRepositoryPort.findById(id);
        if (categoryOpt.isEmpty()) {
            throw new IllegalArgumentException("Category with ID " + id + " not found");
        }

        CategoryModel category = categoryOpt.get();
        // Si tienes soft delete: category.setDeleted(false);
        category.setActive(true);
        category.setUpdatedAt(LocalDateTime.now());
        category.setUpdatedBy("system");

        return categoryRepositoryPort.save(category);
    }

    /**
     * Obtener estadísticas de categorías
     */
    @Transactional(readOnly = true)
    public CategoryStatsDto getCategoryStatistics() {
        List<CategoryModel> allCategories = categoryRepositoryPort.findAll();

        long total = allCategories.size();
        long active = allCategories.stream()
                .filter(category -> category.getActive() != null && category.getActive())
                .count();
        long inactive = allCategories.stream()
                .filter(category -> category.getActive() != null && !category.getActive())
                .count();

        // Si implementas soft delete, cuenta las eliminadas
        long deleted = 0; // Ajustar según tu implementación de soft delete

        return CategoryStatsDto.builder()
                .totalCategories(total)
                .activeCategories(active)
                .inactiveCategories(inactive)
                .deletedCategories(deleted)
                .build();
    }

    /**
     * Buscar categorías por múltiples IDs
     */
    @Transactional(readOnly = true)
    public List<CategoryModel> findByIds(List<UUID> ids) {
        return categoryRepositoryPort.findAllById(ids);
    }

    /**
     * Verificar si una categoría está siendo usada por productos
     * (Útil antes de eliminar)
     */
    @Transactional(readOnly = true)
    public boolean isCategoryInUse(UUID categoryId) {
        // Implementar según tu lógica de negocio
        // Por ejemplo, verificar si hay productos asociados
        return false; // Placeholder
    }

    /**
     * Buscar categorías creadas en un rango de fechas
     */
    @Transactional(readOnly = true)
    public List<CategoryModel> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return categoryRepositoryPort.findAll().stream()
                .filter(category ->
                        category.getCreatedAt() != null &&
                                category.getCreatedAt().isAfter(startDate) &&
                                category.getCreatedAt().isBefore(endDate)
                )
                .collect(Collectors.toList());
    }

    /**
     * Contar categorías por estado
     */
    @Transactional(readOnly = true)
    public long countByActive(Boolean active) {
        return categoryRepositoryPort.findByActive(active).size();
    }
}