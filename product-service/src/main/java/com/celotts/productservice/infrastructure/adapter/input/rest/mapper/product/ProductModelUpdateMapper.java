package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product;



import com.celotts.productservice.domain.model.product.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductUpdateDto;
import com.celotts.productservice.infrastructure.common.util.MapperUtils;

import java.time.LocalDateTime;


public class ProductModelUpdateMapper {
    private ProductModelUpdateMapper() {
        throw new UnsupportedOperationException("Utility class ProductModelUpdateMapper - instantiation not allowed");
    }


    public static void apply(ProductModel model, ProductUpdateDto dto) {
        MapperUtils.updateFieldIfNotNull(dto.getCode(), model::setCode);
        MapperUtils.updateFieldIfNotNull(dto.getDescription(), model::setDescription);
        MapperUtils.updateFieldIfNotNull(dto.getCategoryId(), model::setCategoryId);
        MapperUtils.updateFieldIfNotNull(dto.getUnitCode(), model::setUnitCode);
        MapperUtils.updateFieldIfNotNull(dto.getBrandId(), model::setBrandId);
        MapperUtils.updateFieldIfNotNull(dto.getMinimumStock(), model::setMinimumStock);
        MapperUtils.updateFieldIfNotNull(dto.getCurrentStock(), model::setCurrentStock);
        MapperUtils.updateFieldIfNotNull(dto.getUnitPrice(), model::setUnitPrice);
        MapperUtils.updateFieldIfNotNull(dto.getEnabled(), model::setEnabled);

        model.setUpdatedAt(LocalDateTime.now());
        model.setUpdatedBy(dto.getUpdatedBy() != null ? dto.getUpdatedBy() : "system");
    }
}