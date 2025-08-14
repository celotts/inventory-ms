package com.celotts.productserviceOld.infrastructure.adapter.input.rest.mapper.productCategory;

import com.celotts.productserviceOld.domain.model.ProductCategoryModel;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryCreateDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryResponseDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductCategoryDtoMapper {

    public  ProductCategoryModel toModel(ProductCategoryCreateDto createDto) {
        if(createDto == null) {
            return null;
        }

        return ProductCategoryModel.builder()
                .productId(createDto.getProductId())
                .categoryId(createDto.getCategoryId())
                .assignedAt(createDto.getAssignedAt())
                .createdBy(createDto.getCreatedBy())
                .updatedBy(createDto.getUpdatedBy())
                .createdAt(LocalDateTime.now())   // audit inicial
                .updatedAt(null)
                .build();


    }

    public ProductCategoryResponseDto toDto(ProductCategoryModel model) {
        if(model == null) {
            return null;
        }

        return ProductCategoryResponseDto.builder()
                .id(model.getId())
                .productId(model.getProductId())
                .categoryId(model.getCategoryId())
                .assignedAt(model.getAssignedAt())
                .createdBy(model.getCreatedBy())
                .updatedBy(model.getUpdatedBy())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }




}
