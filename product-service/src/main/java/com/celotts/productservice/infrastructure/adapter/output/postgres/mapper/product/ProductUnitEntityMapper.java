package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.domain.model.ProductUnitModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductUnitEntity;
import org.springframework.stereotype.Component;

@Component     // Spring podrá inyectarlo
public class ProductUnitEntityMapper {

    /* Model ➜ Entity */
    public ProductUnitEntity toEntity(ProductUnitModel model) {
        if (model == null) return null;
        return ProductUnitEntity.builder()
                .id(model.getId())
                .code(model.getCode())
                .name(model.getName())
                .description(model.getDescription())
                .enabled(model.getEnabled())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .createdBy(model.getCreatedBy())
                .updatedBy(model.getUpdatedBy())
                .build();
    }

    /* Entity ➜ Model */
    public ProductUnitModel toModel(ProductUnitEntity entity) {
        if (entity == null) return null;
        return ProductUnitModel.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .enabled(entity.getEnabled())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }
}