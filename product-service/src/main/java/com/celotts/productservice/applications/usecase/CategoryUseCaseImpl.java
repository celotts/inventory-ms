package com.celotts.productservice.applications.usecase;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.domain.port.category.CategoryRepositoryPort;
import com.celotts.productservice.domain.port.category.CategoryUseCase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public List<CategoryModel> findAllById(List<UUID> ids) {
        return repository.findAllById(ids);
    }
}