package com.celotts.productservice.infrastructure.adapter.input.rest.mapper;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductRequestDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductUpdateDTO;

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
        // Usar método helper para evitar repetición
        updateFieldIfNotNull(dto.getCode(), model::setCode);
        updateFieldIfNotNull(dto.getDescription(), model::setDescription);
        updateFieldIfNotNull(dto.getProductTypeCode(), model::setProductTypeCode);
        updateFieldIfNotNull(dto.getUnitCode(), model::setUnitCode);
        updateFieldIfNotNull(dto.getBrandId(), model::setBrandId);
        updateFieldIfNotNull(dto.getMinimumStock(), model::setMinimumStock);
        updateFieldIfNotNull(dto.getCurrentStock(), model::setCurrentStock);
        updateFieldIfNotNull(dto.getUnitPrice(), model::setUnitPrice);
        updateFieldIfNotNull(dto.getEnabled(), model::setEnabled);

        // Campos de auditoría siempre se actualizan
        model.setUpdatedAt(LocalDateTime.now());
        model.setUpdatedBy(dto.getUpdatedBy() != null ? dto.getUpdatedBy() : "system");
    }

    // Método helper para eliminar duplicación
    private static <T> void updateFieldIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}