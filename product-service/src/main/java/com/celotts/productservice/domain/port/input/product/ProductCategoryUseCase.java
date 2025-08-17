package com.celotts.productservice.domain.port.input.product;

import com.celotts.productservice.domain.model.product.ProductCategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryCreateDto;

import java.util.List;
import java.util.UUID;

public interface ProductCategoryUseCase {

    ProductCategoryModel assignCategoryToProduct(ProductCategoryCreateDto dto);
    ProductCategoryModel getById(UUID id);
    List<ProductCategoryModel> getAll();
    void deleteById(UUID id); // físico
    void disableById(UUID id); // lógico
}