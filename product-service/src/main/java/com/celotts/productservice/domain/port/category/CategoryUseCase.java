package com.celotts.productservice.domain.port.category;

import com.celotts.productservice.domain.model.CategoryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryUseCase {
    CategoryModel save(CategoryModel category);
    Optional<CategoryModel> findById(UUID id);
    Optional<CategoryModel> findByName(String name);
    List<CategoryModel> findAll();
    Page<CategoryModel> findAll(Pageable pageable);
    List<CategoryModel> findByNameContaining(String name);
    Page<CategoryModel> findByNameContaining(String name, Pageable pageable);
    Page<CategoryModel> findByActive(Boolean active, Pageable pageable);
    Page<CategoryModel> findByNameContainingAndActive(String name, Boolean active, Pageable pageable);
    boolean existsByName(String name);
    boolean existsById(UUID id);
    void deleteById(UUID id);
    List<CategoryModel> findByActive(Boolean active);
    List<CategoryModel> findByNameOrDescription(String query, int limit);
    List<CategoryModel> findAllById(List<UUID> ids);
}