package com.celotts.productservice.infrastructure.adapter.input.rest.mapper;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductRequestDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductUpdateDTO;

import java.time.LocalDateTime;

public class ProductRequestMapper {

    public static ProductModel toModel(ProductRequestDTO dto) {
        return ProductModel.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .productTypeCode(dto.getProductTypeCode())
                .unitCode(dto.getUnitCode())
                .brandId(dto.getBrandId())
                .minimumStock(dto.getMinimumStock())
                .currentStock(dto.getCurrentStock())
                .unitPrice(dto.getUnitPrice())
                .enabled(dto.getEnabled() != null ? dto.getEnabled() : Boolean.TRUE)
                .createdAt(LocalDateTime.now())
                .createdBy(dto.getCreatedBy() != null ? dto.getCreatedBy() : "system")
                .build();
    }

    public static void updateModelFromDto(ProductModel model, ProductUpdateDTO dto) {
        if (dto.getCode() != null) {
            model.setCode(dto.getCode());
        }

        if (dto.getDescription() != null) {
            model.setDescription(dto.getDescription());
        }
        if (dto.getProductTypeCode() != null) {
            model.setProductTypeCode(dto.getProductTypeCode());
        }
        if (dto.getUnitCode() != null) {
            model.setUnitCode(dto.getUnitCode());
        }
        if (dto.getBrandId() != null) {
            model.setBrandId(dto.getBrandId());
        }
        if (dto.getMinimumStock() != null) {
            model.setMinimumStock(dto.getMinimumStock());
        }
        if (dto.getCurrentStock() != null) {
            model.setCurrentStock(dto.getCurrentStock());
        }
        if (dto.getUnitPrice() != null) {
            model.setUnitPrice(dto.getUnitPrice());
        }
        if (dto.getEnabled() != null) {
            model.setEnabled(dto.getEnabled());
        }

        model.setUpdatedAt(LocalDateTime.now());
        model.setUpdatedBy(dto.getUpdatedBy() != null ? dto.getUpdatedBy() : "system");
    }
}