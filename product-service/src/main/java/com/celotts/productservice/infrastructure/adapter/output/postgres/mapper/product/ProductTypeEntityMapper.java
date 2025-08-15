package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.domain.model.ProductTypeModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductTypeEntityMapper {

    /* Model ➜ Entity */
    public ProductTypeEntity toEntity(ProductTypeModel model) {
        if (model == null) return null;
        return ProductTypeEntity.builder()
                .id(model.getId())
                .code(model.getCode())
                .name(model.getName())
                .description(model.getDescription())   // ← corregido
                .enabled(model.getEnabled())           // si fuera boolean: model.isEnabled()
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .createdBy(model.getCreatedBy())
                .updatedBy(model.getUpdatedBy())       // ← corregido
                .build();
    }

    /* Entity ➜ Model */
    public ProductTypeModel toModel(ProductTypeEntity entity) {
        if (entity == null) return null;
        return ProductTypeModel.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())  // ← corregido
                .enabled(entity.getEnabled())          // si fuera boolean: entity.isEnabled()
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())      // ← corregido
                .build();
    }
}