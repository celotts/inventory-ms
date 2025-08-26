package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productCategory;

import com.celotts.productservice.domain.model.product.ProductCategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ProductCategoryDtoMapper {

    public ProductCategoryModel toModel(ProductCategoryCreateDto createDto) {
        if (createDto == null) return null;
        return ProductCategoryModel.builder()
                .productId(createDto.getProductId())
                .categoryId(createDto.getCategoryId())
                .enabled(createDto.getEnabled()) // null -> default en use case/JPA
                .build();
    }

    public ProductCategoryResponseDto toDto(ProductCategoryModel model) {
        if (model == null) return null;
        return ProductCategoryResponseDto.builder()
                .id(model.getId())
                .productId(model.getProductId())
                .categoryId(model.getCategoryId())
                .enabled(model.getEnabled())
                .assignedAt(model.getAssignedAt())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .createdBy(model.getCreatedBy())
                .updatedBy(model.getUpdatedBy())
                .build();
    }
}