package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product;

import com.celotts.productserviceOld.domain.model.ProductModel;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.product.ProductRequestDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.product.ProductResponseDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.product.ProductUpdateDto;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ProductDtoMapper {

    /**
     * Convierte ProductRequestDTO a ProductModel para crear
     */
    public static ProductModel toModel(ProductRequestDto dto) {
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
                .enabled(dto.getEnabled() != null ? dto.getEnabled() : true)
                .createdBy(dto.getCreatedBy() != null ? dto.getCreatedBy() : "system")
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * Convierte ProductRequestDTO a ProductUpdateDTO
     */
    public static ProductUpdateDto toUpdateDto(ProductRequestDto dto) {
        if (dto == null) return null;

        return ProductUpdateDto.builder()
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
                .updatedBy(dto.getUpdatedBy() != null ? dto.getUpdatedBy() : "system")
                .build();
    }

    /**
     * Convierte ProductModel a ProductResponseDTO
     */
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

    /**
     * Convierte lista de ProductModel a lista de ProductResponseDTO
     */
    //TODO: NO SE USA
    public static List<ProductResponseDto> toResponseDtoList(List<ProductModel> models) {
        if (models == null) return null;
        return models.stream()
                .map(ProductDtoMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}