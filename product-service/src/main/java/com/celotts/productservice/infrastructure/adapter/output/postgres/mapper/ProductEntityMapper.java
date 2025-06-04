package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper;

import com.celotts.productservice.domain.model.ProductModel;  // ✅ Cambiar Product por ProductModel
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.ProductEntity;
import org.springframework.stereotype.Component;



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

        return new ProductModel(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getDescription(),
                entity.getProductTypeCode(),
                entity.getUnitCode(),
                entity.getBrandId(),
                entity.getMinimumStock(),
                entity.getCurrentStock(),
                entity.getUnitPrice(),
                entity.getEnabled(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getCreatedBy(),
                entity.getUpdatedBy(),
                entity.getEnabled() // ← aquí el campo faltante `withEnabled`
        );
    }
}