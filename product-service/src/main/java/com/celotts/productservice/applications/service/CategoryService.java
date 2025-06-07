package com.celotts.productservice.applications.service;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.domain.port.CategoryRepositoryPort;  // ✅ Corregido: sin "output"
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepositoryPort categoryRepositoryPort;

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
}