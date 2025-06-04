package com.celotts.productservice.domain.port;

import com.celotts.productservice.domain.model.ProductCategoryModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductCategoryPort {
    ProductCategoryModel create(ProductCategoryModel model);
    List<ProductCategoryModel> findAll();
    Optional<ProductCategoryModel> findById(UUID id);
    List<ProductCategoryModel> findByProductId(UUID productId);
    void delete(UUID id);
    boolean existsById(UUID id);
}