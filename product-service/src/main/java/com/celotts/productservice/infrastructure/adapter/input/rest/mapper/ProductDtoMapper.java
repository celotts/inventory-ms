package com.celotts.productservice.infrastructure.adapter.input.rest.mapper;

import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductRequestDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductUpdateDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductDtoMapper {
    public ProductUpdateDTO toUpdateDto(ProductRequestDTO requestDto) {
        return ProductUpdateDTO.builder()
                .code(requestDto.getCode())
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .categoryId(requestDto.getCategoryId())  // ✅ AGREGAR esta línea
                .unitCode(requestDto.getUnitCode())
                .brandId(requestDto.getBrandId())
                .minimumStock(requestDto.getMinimumStock())
                .currentStock(requestDto.getCurrentStock())
                .unitPrice(requestDto.getUnitPrice())
                .enabled(requestDto.getEnabled())
                .updatedBy(requestDto.getUpdatedBy())
                .build();
    }
}