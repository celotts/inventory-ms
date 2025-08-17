package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.domain.model.product.ProductTypeModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductTypeEntityMapper {

    public ProductTypeEntity toEntity(ProductTypeModel model) {
        if (model == null) return null;
        return ProductTypeEntity.builder()
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

    public ProductTypeModel toModel(ProductTypeEntity entity) {
        if (entity == null) return null;
        return ProductTypeModel.builder()
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