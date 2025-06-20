package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductEntityMapper {

    public static ProductEntity toEntity(ProductModel productModel) {
        if (productModel == null) {
            return null;
        }

        return ProductEntity.builder()
                .id(productModel.getId())
                .code(productModel.getCode())
                .name(productModel.getName())
                .description(productModel.getDescription())
                .categoryId(productModel.getCategoryId())  // ✅ AGREGAR
                .unitCode(productModel.getUnitCode())
                .brandId(productModel.getBrandId())
                .minimumStock(productModel.getMinimumStock())
                .currentStock(productModel.getCurrentStock())
                .unitPrice(productModel.getUnitPrice())
                .enabled(productModel.getEnabled())
                .createdAt(productModel.getCreatedAt())
                .updatedAt(productModel.getUpdatedAt())
                .createdBy(productModel.getCreatedBy())
                .updatedBy(productModel.getUpdatedBy())
                .build();
    }

    public ProductModel toModel(ProductEntity entity) {
        if (entity == null) {
            return null;
        }

        return ProductModel.builder()  // ✅ Usar builder pattern
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .categoryId(entity.getCategoryId())  // ✅ AGREGAR
                .unitCode(entity.getUnitCode())
                .brandId(entity.getBrandId())
                .minimumStock(entity.getMinimumStock())
                .currentStock(entity.getCurrentStock())
                .unitPrice(entity.getUnitPrice())
                .enabled(entity.getEnabled())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }


}