package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductBrandEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductBrandEntityMapper {

    public ProductBrandEntity toEntity(ProductBrandModel productBrandModel) {
        if (productBrandModel == null) {
            return null;
        }

        return ProductBrandEntity.builder()
                .id(productBrandModel.getId())
                .name(productBrandModel.getName())
                .description(productBrandModel.getDescription())
                .enabled(productBrandModel.getEnabled())
                .createdBy(productBrandModel.getCreatedBy())
                .updatedBy(productBrandModel.getUpdatedBy())
                .createdAt(productBrandModel.getCreatedAt())
                .updatedAt(productBrandModel.getUpdatedAt())
                .build();
    }

    public ProductBrandModel toModel(ProductBrandEntity entity) { // ✅ Cambiar ProductBrandEntity por ProductBrand
        if (entity == null) {
            return null;
        }

        return ProductBrandModel.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .enabled(entity.getEnabled())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public ProductBrandResponseDto toResponseDto(ProductBrandEntity entity) {
        if (entity == null) {
            return null;
        }

        return ProductBrandResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .enabled(entity.getEnabled())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}