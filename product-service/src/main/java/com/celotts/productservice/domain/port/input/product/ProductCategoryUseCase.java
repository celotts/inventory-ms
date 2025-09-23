package com.celotts.productservice.domain.port.input.product;

import com.celotts.productservice.domain.model.product.ProductCategoryModel;

import java.util.List;
import java.util.UUID;

public interface ProductCategoryUseCase {
    ProductCategoryModel assignCategoryToProduct(ProductCategoryModel model);
    ProductCategoryModel getById(UUID id);
    List<ProductCategoryModel> getAll();
    void disableById(UUID id);
    void deleteById(UUID id);
}