package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductRequestDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductUpdateDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDtoMapper {

    /**
     * Convierte ProductRequestDTO a ProductModel para crear
     */
    public static ProductModel toModel(ProductRequestDTO dto) {
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
    public static ProductUpdateDTO toUpdateDto(ProductRequestDTO dto) {
        if (dto == null) return null;

        return ProductUpdateDTO.builder()
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
    public static ProductResponseDTO toResponseDto(ProductModel model) {
        if (model == null) return null;

        return ProductResponseDTO.builder()
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
    public static List<ProductResponseDTO> toResponseDtoList(List<ProductModel> models) {
        if (models == null) return null;
        return models.stream()
                .map(ProductDtoMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}