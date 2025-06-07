package com.celotts.productservice.domain.port;

import com.celotts.productservice.domain.model.CategoryModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepositoryPort {

    // Métodos básicos CRUD
    CategoryModel save(CategoryModel category);
    Optional<CategoryModel> findById(UUID id);
    List<CategoryModel> findAll();
    void deleteById(UUID id);
    boolean existsById(UUID id);
    boolean existsByName(String name);

    // Métodos de búsqueda adicionales
    Optional<CategoryModel> findByName(String name);
    List<CategoryModel> findByNameContaining(String name);
}