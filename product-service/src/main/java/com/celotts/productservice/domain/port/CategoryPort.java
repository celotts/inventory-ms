package com.celotts.productservice.domain.port;

import com.celotts.productservice.domain.model.CategoryModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryPort {
    CategoryModel create(CategoryModel category);
    List<CategoryModel> findAll();
    Optional<CategoryModel> findById(UUID id);
    CategoryModel update(UUID id, CategoryModel category);

    void delete(UUID id);
    boolean existsById(UUID id);

}