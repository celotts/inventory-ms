package com.celotts.productservice.domain.port.category;

import com.celotts.productservice.domain.model.CategoryModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryPort {  // Siguiendo tu patrón, no CategoryRepositoryPort
    CategoryModel save(CategoryModel category);
    Optional<CategoryModel> findById(UUID id);
    Optional<CategoryModel> findByName(String name);
    List<CategoryModel> findAll();
    List<CategoryModel> findByNameContaining(String name);
    boolean existsByName(String name);
    void deleteById(UUID id);
    boolean existsById(UUID id);
}