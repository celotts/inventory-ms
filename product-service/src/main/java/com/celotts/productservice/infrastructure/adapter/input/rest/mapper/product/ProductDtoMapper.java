package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product;

import com.celotts.productservice.domain.model.product.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductResponseDto;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ProductDtoMapper {

    // Crear -> Model
    public static ProductModel toModel(ProductCreateDto dto) {
        if (dto == null) return null;
        return ProductModel.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .unitCode(dto.getUnitCode())
                .brandId(dto.getBrandId())
                .categoryId(dto.getCategoryId())
                .minimumStock(dto.getMinimumStock())
                .currentStock(dto.getCurrentStock())
                .unitPrice(dto.getUnitPrice())
                .enabled(Boolean.TRUE.equals(dto.getEnabled()))
                .createdBy(dto.getCreatedBy())
                .build();
    }

    // Update -> Model (parcial)
    public static ProductModel toModel(ProductUpdateDto dto) {
        if (dto == null) return null;
        return ProductModel.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .unitCode(dto.getUnitCode())
                .brandId(dto.getBrandId())
                .categoryId(dto.getCategoryId())
                .minimumStock(dto.getMinimumStock())
                .currentStock(dto.getCurrentStock())
                .unitPrice(dto.getUnitPrice())
                .enabled(dto.getEnabled())
                .updatedBy(dto.getUpdatedBy())
                .build();
    }

    // Model -> Response
    public static ProductResponseDto toResponseDto(ProductModel model) {
        if (model == null) return null;
        return ProductResponseDto.builder()
                .id(model.getId())
                .code(model.getCode())
                .name(model.getName())
                .description(model.getDescription())
                .unitCode(model.getUnitCode())
                .brandId(model.getBrandId())
                .categoryId(model.getCategoryId())
                .minimumStock(model.getMinimumStock())
                .currentStock(model.getCurrentStock())
                .unitPrice(model.getUnitPrice())
                .enabled(model.getEnabled())
                .createdBy(model.getCreatedBy())
                .updatedBy(model.getUpdatedBy())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    public static List<ProductResponseDto> toResponseDtoList(List<ProductModel> models) {
        if (models == null) return null;
        return models.stream().map(ProductDtoMapper::toResponseDto).collect(Collectors.toList());
    }
}