package com.celotts.productservice.domain.port.output.product;

import com.celotts.productservice.domain.model.product.ProductCategoryModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ProductCategoryRepositoryPort {

    ProductCategoryModel save(ProductCategoryModel model);

    Optional<ProductCategoryModel> findById(UUID id);

    Optional<ProductCategoryModel> findByProductIdAndCategoryId(UUID productId, UUID categoryId);

    boolean existsByProductIdAndCategoryId(UUID productId, UUID categoryId);

    List<ProductCategoryModel> findAllByProductId(UUID id);

    void deleteById(UUID id);

    void deleteByProductIdAndCategoryId(UUID productId, UUID categoryId);

    void disableById(UUID id);
    void enableById(UUID id);

}
