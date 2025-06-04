package com.celotts.productservice.applications.service;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.domain.port.CategoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryPort categoryPort;

    public CategoryModel create(CategoryModel model) {
        return categoryPort.create(model);
    }

    public List<CategoryModel> getAll() {
        return categoryPort.findAll();
    }

    public CategoryModel getById(UUID id) {
        return categoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    public CategoryModel update(UUID id, CategoryModel model) {
        if (!categoryPort.existsById(id)) {
            throw new IllegalArgumentException("Category not found");
        }
        return categoryPort.update(id, model);
    }

    public void delete(UUID id) {
        if (!categoryPort.existsById(id)) {
            throw new IllegalArgumentException("Category not found");
        }
        categoryPort.delete(id);
    }
}