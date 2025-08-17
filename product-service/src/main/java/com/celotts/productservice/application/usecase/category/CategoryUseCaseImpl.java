package com.celotts.productservice.application.usecase.category;

import com.celotts.productservice.domain.model.category.CategoryModel;
import com.celotts.productservice.domain.model.category.CategoryStats;
import com.celotts.productservice.domain.port.input.category.CategoryUseCase;
import com.celotts.productservice.domain.port.output.category.CategoryRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryUseCaseImpl implements CategoryUseCase {

    private final CategoryRepositoryPort repository;

    // La implementación depende del puerto, no del nombre del adapter
    //public CategoryUseCaseImpl(CategoryRepositoryPort repository) {
        //this.repository = repository;
    //}

    @Override
    public CategoryModel save(CategoryModel category) {
        return repository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryModel> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryModel> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryModel> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryModel> findAllById(List<UUID> ids) {
        return repository.findAllById(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryModel> findByNameContaining(String name) {
        return repository.findByNameContaining(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryModel> searchByNameOrDescription(String query, int limit) {
        return repository.findByNameOrDescription(query, limit);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryModel> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryModel> findByNameContaining(String name, Pageable pageable) {
        return repository.findByNameContaining(name, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryModel> findByActive(Boolean active, Pageable pageable) {
        return repository.findByActive(active, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryModel> findByNameContainingAndActive(String name, Boolean active, Pageable pageable) {
        return repository.findByNameContainingAndActive(name, active, pageable);
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public CategoryStats getCategoryStatistics() {
        long total = repository.count();           // mejor que findAll().size()
        long active = repository.countByActive(true);
        long inactive = repository.countByActive(false);
        return CategoryStats.builder()
                .totalCategories(total)
                .activeCategories(active)
                .inactiveCategories(inactive)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByActive(boolean active) {
        return repository.countByActive(active);
    }
}