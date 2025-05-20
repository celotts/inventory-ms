package com.celotts.productservice.infrastructure.adapter.input.rest.mapper;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductResponseDTO;

public class ProductResponseMapper {

    public static ProductResponseDTO toResponse(ProductModel model) {
        return ProductResponseDTO.builder()
                .id(model.getId())
                .code(model.getCode())
                .description(model.getDescription())
                .productType(model.getProductTypeCode())
                .unit(model.getUnitCode())
                .brandId(model.getBrandId())
                .minimumStock(model.getMinimumStock() != null ? model.getMinimumStock().doubleValue() : null)
                .currentStock(model.getCurrentStock() != null ? model.getCurrentStock().doubleValue() : null)
                .unitPrice(model.getUnitPrice() != null ? model.getUnitPrice().doubleValue() : null)
                .enabled(model.getEnabled())
                .build();
    }
}