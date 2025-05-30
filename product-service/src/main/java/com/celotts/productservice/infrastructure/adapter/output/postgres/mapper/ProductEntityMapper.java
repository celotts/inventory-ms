package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.Product;

public class ProductEntityMapper {

    public static Product toEntity(ProductModel model) {
        if (model == null) return null;

        Product product = new Product();

        // Solo establecer ID para updates cuando ya existe
        if (model.getId() != null) {
            product.setId(model.getId());
        }

        product.setCode(model.getCode());
        product.setName(model.getName()); // ← FIX: Usar name del model, no description
        product.setDescription(model.getDescription());

        // Mapeo directo sin entidades relacionadas
        product.setProductTypeCode(model.getProductTypeCode());
        product.setUnitCode(model.getUnitCode());
        product.setBrandId(model.getBrandId());

        product.setMinimumStock(model.getMinimumStock());
        product.setCurrentStock(model.getCurrentStock());
        product.setUnitPrice(model.getUnitPrice());
        product.setEnabled(model.getEnabled());
        product.setCreatedAt(model.getCreatedAt());
        product.setUpdatedAt(model.getUpdatedAt());
        product.setCreatedBy(model.getCreatedBy());
        product.setUpdatedBy(model.getUpdatedBy());

        return product;
    }

    public static ProductModel toModel(Product entity) {
        if (entity == null) return null;

        return ProductModel.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName()) // ← FIX: Mapear name también
                .description(entity.getDescription())
                .productTypeCode(entity.getProductTypeCode())
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