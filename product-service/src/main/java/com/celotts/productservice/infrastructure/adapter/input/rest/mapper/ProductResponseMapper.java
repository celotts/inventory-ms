package com.celotts.productservice.infrastructure.adapter.input.rest.mapper;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductResponseDTO;

public class ProductResponseMapper {

    public static ProductResponseDTO toResponse(ProductModel model) {
        if (model == null) {
            return null;
        }

        return ProductResponseDTO.builder()
                .id(model.getId())
                .code(model.getCode())
                .description(model.getDescription())
                .productTypeCode(model.getProductTypeCode())
                .unitCode(model.getUnitCode())
                .brandId(model.getBrandId())
                .minimumStock(model.getMinimumStock())
                .currentStock(model.getCurrentStock())
                .unitPrice(model.getUnitPrice())
                .enabled(model.getEnabled())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .createdBy(model.getCreatedBy())
                .updatedBy(model.getUpdatedBy())
                .lowStock(model.getCurrentStock() != null && model.getMinimumStock() != null &&
                        model.getCurrentStock() < model.getMinimumStock())
                .build();
    }
}