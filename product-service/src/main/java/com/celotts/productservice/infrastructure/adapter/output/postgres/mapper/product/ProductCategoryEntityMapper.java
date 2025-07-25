package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.domain.model.ProductCategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryResponseDto;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductCategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductCategoryEntityMapper {

    public ProductCategoryEntity toEntity(ProductCategoryModel model) {
        if (model == null) {
            return null;
        }

        return ProductCategoryEntity.builder()
                .id(model.getId())
                .productId(model.getProductId())
                .categoryId(model.getCategoryId())
                .assignedAt(model.getAssignedAt())
                .enabled(model.getEnabled())
                .createdBy(model.getCreatedBy())
                .updatedBy(model.getUpdatedBy())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    public ProductCategoryModel toModel(ProductCategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        return ProductCategoryModel.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .categoryId(entity.getCategoryId())
                .assignedAt(entity.getAssignedAt())
                .enabled(entity.getEnabled())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public ProductCategoryResponseDto toResponseDto(ProductCategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        return ProductCategoryResponseDto.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .categoryId(entity.getCategoryId())
                .assignedAt(entity.getAssignedAt())
                .enabled(entity.getEnabled())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}