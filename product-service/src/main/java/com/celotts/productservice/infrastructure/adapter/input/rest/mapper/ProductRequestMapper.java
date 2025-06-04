package com.celotts.productservice.infrastructure.adapter.input.rest.mapper;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductRequestDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductUpdateDTO;
import static com.celotts.productservice.infrastructure.common.util.MapperUtils.updateFieldIfNotNull;
import java.time.LocalDateTime;
import java.util.function.Consumer;

public class ProductRequestMapper {

    public static ProductModel toModel(ProductRequestDTO dto) {
        return ProductModel.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .productTypeCode(dto.getProductTypeCode())
                .unitCode(dto.getUnitCode())
                .brandId(dto.getBrandId())
                .minimumStock(dto.getMinimumStock())
                .currentStock(dto.getCurrentStock())
                .unitPrice(dto.getUnitPrice())
                .enabled(dto.getEnabled() != null ? dto.getEnabled() : Boolean.TRUE)
                .createdAt(LocalDateTime.now())
                .createdBy(dto.getCreatedBy() != null ? dto.getCreatedBy() : "system")
                .build();
    }

    public static void updateModelFromDto(ProductModel model, ProductUpdateDTO dto) {
        ProductModelUpdateMapper.apply(model, dto);
    }


}