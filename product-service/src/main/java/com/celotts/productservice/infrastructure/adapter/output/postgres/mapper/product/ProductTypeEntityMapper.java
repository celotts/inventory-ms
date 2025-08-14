package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productserviceOld.domain.model.ProductTypeModel;

import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;
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
                .description(model.getDescpiption())
                .enabled(model.getEnabled())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .createdBy(model.getCreatedBy())
                .updatedBy(model.getUpdateBy())
                .build();
    }

    /* Entity ➜ Model */
    public ProductTypeModel toModel(ProductTypeEntity entity) {
        if (entity == null) return null;
        return ProductTypeModel.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .descpiption(entity.getDescription())
                .enabled(entity.getEnabled())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updateBy(entity.getUpdatedBy())
                .build();
    }
}
