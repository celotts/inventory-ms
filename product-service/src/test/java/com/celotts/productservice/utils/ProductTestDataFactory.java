package com.celotts.productservice.utils;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductUpdateDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ProductTestDataFactory {

    public static ProductCreateDto createProductDtoMock(UUID brandId, UUID categoryId) {
        return ProductCreateDto.builder()
                .code("TEST-CODE")
                .name("Test Product")
                .description("Mock product for testing")
                .unitCode("U01")
                .brandId(brandId)
                .categoryId(categoryId)
                .minimumStock(10)
                .currentStock(50)
                .unitPrice(BigDecimal.valueOf(99.99))
                .enabled(true)
                .createdBy("tester")
                .build();
    }

    public static ProductModel createProductModelMock(UUID id, ProductCreateDto dto) {
        return ProductModel.builder()
                .id(id)
                .code(dto.getCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .unitCode(dto.getUnitCode())
                .brandId(dto.getBrandId())
                .categoryId(dto.getCategoryId())
                .minimumStock(dto.getMinimumStock())
                .currentStock(dto.getCurrentStock())
                .unitPrice(dto.getUnitPrice())
                .enabled(dto.getEnabled())
                .createdBy(dto.getCreatedBy())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ProductUpdateDto createProductUpdateDtoMock(ProductModel model) {
        return ProductUpdateDto.builder()
                .code(model.getCode() + "-UPDATED")
                .name(model.getName() + " Updated")
                .unitCode(model.getUnitCode())
                .brandId(model.getBrandId())
                .categoryId(model.getCategoryId())
                .build();
    }
}