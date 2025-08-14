package com.celotts.productservice.applications.usecase.category;

import com.celotts.productserviceOld.domain.model.CategoryModel;
import com.celotts.productserviceOld.domain.port.category.output.CategoryRepositoryPort;
import com.celotts.productserviceOld.domain.port.category.usecase.CategoryUseCase;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.category.CategoryStatusDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryUseCaseImpl implements CategoryUseCase {

    private final CategoryRepositoryPort repository;

    // Constructor con @Qualifier para resolver ambigüedad
    public CategoryUseCaseImpl(
            @Qualifier("categoryRepositoryAdapter") CategoryRepositoryPort repository
    ) {
        this.repository = repository;
    }

    @Override
    public CategoryModel save(CategoryModel category) {
        return repository.save(category);
    }

    @Override
    public Optional<CategoryModel> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Optional<CategoryModel> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<CategoryModel> findAll() {
        return repository.findAll();
    }

    @Override
    public List<CategoryModel> findByNameContaining(String name) {
        return repository.findByNameContaining(name);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public List<CategoryModel> findByActive(Boolean active) {
        return repository.findByActive(active);
    }

    @Override
    public Page<CategoryModel> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<CategoryModel> findByNameContaining(String name, Pageable pageable) {
        return repository.findByNameContaining(name, pageable);
    }

    @Override
    public Page<CategoryModel> findByActive(Boolean active, Pageable pageable) {
        return repository.findByActive(active, pageable);
    }

    @Override
    public Page<CategoryModel> findByNameContainingAndActive(String name, Boolean active, Pageable pageable) {
        return repository.findByNameContainingAndActive(name, active, pageable);
    }

    @Override
    public List<CategoryModel> findByNameOrDescription(String query, int limit) {
        return repository.findByNameOrDescription(query, limit);
    }


    @Override
    public List<CategoryModel> searchByNameOrDescription(String query, int limit) {
        return repository.findByNameOrDescription(query, limit);
    }

    @Override
    public List<CategoryModel> findAllById(List<UUID> ids) {
        return repository.findAllById(ids);
    }

    /**
     * Devuelve categorías paginadas filtradas por nombre y/o estado activo.
     * Este método depende internamente de los métodos:
     * - findByNameContaining
     * - findByActive
     * - findByNameContainingAndActive
     *
     * ⚠️ No eliminar esos métodos del puerto o repositorio aunque el IDE los marque sin usos directos.
     */
    @Override
    public Page<CategoryModel> findAllPaginated(String name, Boolean active, Pageable pageable) {
        return repository.findAllPaginated(name, active, pageable);
    }

    @Override
    public CategoryModel updateStatus(UUID id, Boolean active) {
        CategoryModel existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        existing.setActive(active);
        existing.setUpdatedAt(LocalDateTime.now());

        return repository.save(existing);
    }

    @Override
    public CategoryModel restore(UUID id) {
        CategoryModel existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        existing.setDeleted(false);
        existing.setUpdatedAt(LocalDateTime.now());

        return repository.save(existing);
    }

    @Override
    public void permanentDelete(UUID id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Category not found");
        }
        repository.deleteById(id);
    }

    @Override
    public CategoryStatusDto getCategoryStatistics() {
        long total = repository.findAll().size();  // Podrías optimizar también a un count si lo tienes
        long active = repository.countByActive(true);
        long inactive = repository.countByActive(false);

        return CategoryStatusDto.builder()
                .totalCategories(total)
                .activeCategories(active)
                .inactiveCategories(inactive)
                .build();
    }

    @Override
    public long countByActive(boolean active) {
        return repository.countByActive(active);
    }







}