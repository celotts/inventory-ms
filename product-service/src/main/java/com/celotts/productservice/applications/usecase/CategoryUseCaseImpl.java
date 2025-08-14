package com.celotts.productservice.applications.usecase;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.domain.port.category.output.CategoryRepositoryPort;
import com.celotts.productservice.domain.port.category.usecase.CategoryUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryStatusDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class CategoryUseCaseImpl implements CategoryUseCase {

    private final CategoryRepositoryPort repository;

    public CategoryUseCaseImpl(CategoryRepositoryPort repository) {
        this.repository = repository;
    }

    // En CategoryUseCaseImpl
    @Override
    public CategoryModel create(CategoryModel category) {
        category.setId(null);
        return save(category);
    }

    @Override
    public CategoryModel update(UUID id, CategoryModel category) {
        category.setId(id);
        return save(category);
    }

    // Crear / Actualizar (lógica migrada)
    @Override
    public CategoryModel save(CategoryModel category) {
        if (category.getId() == null) { // Crear
            if (existsByName(category.getName())) {
                throw new IllegalArgumentException(
                        "Category with name '" + category.getName() + "' already exists");
            }
            category.setCreatedAt(LocalDateTime.now());
            category.setCreatedBy(getCurrentUsername());
            return repository.save(category);
        } else { // Actualizar
            CategoryModel existing = repository.findById(category.getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Category with ID " + category.getId() + " not found"));

            // Merge mínimo; si prefieres un mapper, hazlo en el controller.
            if (category.getName() != null)        existing.setName(category.getName());
            if (category.getDescription() != null) existing.setDescription(category.getDescription());
            if (category.getActive() != null)      existing.setActive(category.getActive());

            existing.setUpdatedAt(LocalDateTime.now());
            existing.setUpdatedBy(getCurrentUsername());
            return repository.save(existing);
        }
    }

    // ----- Lecturas básicas -----
    @Override @Transactional(readOnly = true)
    public Optional<CategoryModel> findById(UUID id) { return repository.findById(id); }

    @Override @Transactional(readOnly = true)
    public Optional<CategoryModel> findByName(String name) { return repository.findByName(name); }

    @Override @Transactional(readOnly = true)
    public List<CategoryModel> findAll() { return repository.findAll(); }

    @Override @Transactional(readOnly = true)
    public Page<CategoryModel> findAll(Pageable pageable) { return repository.findAll(pageable); }

    @Override @Transactional(readOnly = true)
    public List<CategoryModel> findByNameContaining(String name) { return repository.findByNameContaining(name); }

    @Override @Transactional(readOnly = true)
    public boolean existsByName(String name) { return repository.existsByName(name); }

    @Override @Transactional(readOnly = true)
    public boolean existsById(UUID id) { return repository.existsById(id); }

    @Override
    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Category with ID " + id + " not found");
        }
        repository.deleteById(id);
    }

    @Override @Transactional(readOnly = true)
    public List<CategoryModel> findAllById(List<UUID> ids) { return repository.findAllById(ids); }

    // ----- Paginación y filtros -----
    @Override @Transactional(readOnly = true)
    public Page<CategoryModel> findAllPaginated(String name, Boolean active, Pageable pageable) {
        return repository.findAllPaginated(name, active, pageable);
    }

    @Override @Transactional(readOnly = true)
    public List<CategoryModel> findByActive(Boolean active) { return repository.findByActive(active); }

    @Override @Transactional(readOnly = true)
    public Page<CategoryModel> findByNameContaining(String name, Pageable pageable) {
        return repository.findByNameContaining(name, pageable);
    }

    @Override @Transactional(readOnly = true)
    public Page<CategoryModel> findByActive(Boolean active, Pageable pageable) {
        return repository.findByActive(active, pageable);
    }

    @Override @Transactional(readOnly = true)
    public Page<CategoryModel> findByNameContainingAndActive(String name, Boolean active, Pageable pageable) {
        return repository.findByNameContainingAndActive(name, active, pageable);
    }

    // ----- Búsquedas libres -----
    @Override @Transactional(readOnly = true)
    public List<CategoryModel> findByNameOrDescription(String query, int limit) {
        return repository.findByNameOrDescription(query, limit);
    }

    @Override @Transactional(readOnly = true)
    public List<CategoryModel> searchByNameOrDescription(String query, int limit) {
        return repository.findByNameOrDescription(query, limit);
    }

    // ----- Actualizaciones específicas -----
    @Override
    public CategoryModel updateStatus(UUID id, Boolean active) {
        CategoryModel category = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category with ID " + id + " not found"));
        category.setActive(active);
        category.setUpdatedAt(LocalDateTime.now());
        category.setUpdatedBy(getCurrentUsername());
        return repository.save(category);
    }

    @Override
    public CategoryModel restore(UUID id) {
        CategoryModel category = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        category.setDeleted(false);
        category.setUpdatedAt(LocalDateTime.now());
        category.setUpdatedBy(getCurrentUsername());
        return repository.save(category);
    }

    @Override
    public void permanentDelete(UUID id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Category not found");
        }
        repository.deleteById(id); // o repository.permanentDelete(id) si lo tienes
    }

    // ----- Estadísticas -----
    @Override @Transactional(readOnly = true)
    public CategoryStatusDto getCategoryStatistics() {
        long total = repository.findAll().size();
        long active = repository.countByActive(true);
        long inactive = repository.countByActive(false);
        return CategoryStatusDto.builder()
                .totalCategories(total)
                .activeCategories(active)
                .inactiveCategories(inactive)
                .build();
    }

    @Override @Transactional(readOnly = true)
    public long countByActive(boolean active) { return repository.countByActive(active); }

    // ----- Util -----
    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "system";
    }
}