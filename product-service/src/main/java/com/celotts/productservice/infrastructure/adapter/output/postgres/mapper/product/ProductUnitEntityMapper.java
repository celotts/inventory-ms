package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.domain.model.product.ProductUnitModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductUnitEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductUnitEntityMapper {

    /* Model ➜ Entity */
    public ProductUnitEntity toEntity(ProductUnitModel model) {
        if (model == null) return null;

        // 1) Construye solo campos propios del entity
        ProductUnitEntity e = ProductUnitEntity.builder()
                .id(model.getId())
                .code(model.getCode())
                .name(model.getName())
                .description(model.getDescription())
                .symbol(model.getSymbol())
                .enabled(model.getEnabled())
                .build();

        // 2) Auditoría: setea mediante setters heredados de BaseEntity
        e.setCreatedAt(model.getCreatedAt());
        e.setUpdatedAt(model.getUpdatedAt());
        e.setCreatedBy(model.getCreatedBy());
        e.setUpdatedBy(model.getUpdatedBy());

        return e;
    }

    /* Entity ➜ Model */
    public ProductUnitModel toModel(ProductUnitEntity entity) {
        if (entity == null) return null;

        return ProductUnitModel.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .symbol(entity.getSymbol())
                .enabled(entity.getEnabled())
                // Auditoría desde BaseEntity (getters heredados)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }
}