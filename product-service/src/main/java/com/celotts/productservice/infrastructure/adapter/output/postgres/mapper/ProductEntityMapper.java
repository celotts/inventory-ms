package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper;

import com.celotts.productservice.domain.model.ProductModel;  // ✅ Cambiar Product por ProductModel
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component  // ✅ Agregar esta anotación para hacer el mapper un bean
public class ProductEntityMapper {

    public static ProductEntity toEntity(ProductModel productModel) {  // ✅ Cambiar Product por ProductModel
        if (productModel == null) {
            return null;
        }

        return ProductEntity.builder()
                .id(productModel.getId())
                .code(productModel.getCode())
                .name(productModel.getName())
                .description(productModel.getDescription())
                .unitPrice(productModel.getUnitPrice())
                .currentStock(productModel.getCurrentStock())
                .minimumStock(productModel.getMinimumStock())
                .enabled(productModel.getEnabled())
                .unitCode(productModel.getUnitCode())
                .productTypeCode(productModel.getProductTypeCode())
                .brandId(productModel.getBrandId())
                .createdAt(productModel.getCreatedAt())
                .updatedAt(productModel.getUpdatedAt())
                .createdBy(productModel.getCreatedBy())
                .updatedBy(productModel.getUpdatedBy())
                .build();
    }

    public ProductModel toModel(ProductEntity entity) {  // ✅ Cambiar Product por ProductModel
        if (entity == null) {
            return null;
        }

        return ProductModel.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .unitPrice(entity.getUnitPrice())
                .currentStock(entity.getCurrentStock())
                .minimumStock(entity.getMinimumStock())
                .enabled(entity.getEnabled())
                .unitCode(entity.getUnitCode())
                .productTypeCode(entity.getProductTypeCode())
                .brandId(entity.getBrandId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }
}