package com.celotts.productservice.infrastructure.adapter.input.rest.mapper;

import com.celotts.productservice.domain.model.ProductCategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductCategoryResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductCategoryResponseMapper {
    public ProductCategoryResponseDTO toDto(ProductCategoryModel model) {
        return ProductCategoryResponseDTO.builder()
                .id(model.id())
                .productId(model.productId())
                .categoryId(model.categoryId())
                .createdAt(model.createdAt())
                .build();
    }
}