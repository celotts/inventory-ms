package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.domain.model.ProductTagModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTagEntity; // <- cambia esto
import org.springframework.stereotype.Component;

@Component
public class ProductTagEntityMapper {

    public ProductTagModel toDomain(ProductTagEntity e) {
        if (e == null) return null;
        return ProductTagModel.builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .enabled(e.getEnabled())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .createdBy(e.getCreatedBy())
                .updatedBy(e.getUpdatedBy())
                .build();
    }

    public ProductTagEntity toEntity(ProductTagModel m) {
        if (m == null) return null;
        return ProductTagEntity.builder()
                .id(m.getId())
                .name(m.getName())
                .description(m.getDescription())
                .enabled(m.getEnabled())
                .createdAt(m.getCreatedAt())
                .updatedAt(m.getUpdatedAt())
                .createdBy(m.getCreatedBy())
                .updatedBy(m.getUpdatedBy())
                .build();
    }

    public void updateEntity(ProductTagEntity target, ProductTagModel source) {
        if (target == null || source == null) return;
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setEnabled(source.getEnabled());
        target.setUpdatedBy(source.getUpdatedBy());
        target.setUpdatedAt(source.getUpdatedAt());
    }
}