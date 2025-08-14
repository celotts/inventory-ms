package com.celotts.productservice.domain.port.input.product;

import com.celotts.productserviceOld.domain.model.ProductCategoryModel;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryCreateDto;

import java.util.List;
import java.util.UUID;

public interface ProductCategoryUseCase {

    ProductCategoryModel assignCategoryToProduct(ProductCategoryCreateDto dto);
    ProductCategoryModel getById(UUID id);
    List<ProductCategoryModel> getAll();
    void deleteById(UUID id); // físico
    void disableById(UUID id); // lógico
}