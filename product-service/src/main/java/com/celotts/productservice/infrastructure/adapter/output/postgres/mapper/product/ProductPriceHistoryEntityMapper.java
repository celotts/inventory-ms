package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.domain.model.product.ProductPriceHistoryModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductPriceHistoryEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductPriceHistoryEntityMapper {

    // Entity → Model
    public ProductPriceHistoryModel toModel(ProductPriceHistoryEntity entity) {
        if (entity == null) return null;

        return ProductPriceHistoryModel.builder()
                .id(entity.getId())
                .productId(entity.getProduct() != null ? entity.getProduct().getId() : null)
                .price(entity.getPrice())
                .enabled(entity.getEnabled())
                .changedAt(entity.getChangedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    // Model → Entity
    public ProductPriceHistoryEntity toEntity(ProductPriceHistoryModel model) {
        if (model == null) return null;

        ProductPriceHistoryEntity entity = new ProductPriceHistoryEntity();
        entity.setId(model.getId());

        // ⚠️ Solo asignamos productId (no cargamos todo el ProductEntity)
        if (model.getProductId() != null) {
            ProductEntity productRef = new ProductEntity();
            productRef.setId(model.getProductId());
            entity.setProduct(productRef);
        }

        entity.setPrice(model.getPrice());
        entity.setEnabled(model.getEnabled());
        entity.setChangedAt(model.getChangedAt());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedBy(model.getUpdatedBy());

        return entity;
    }
}