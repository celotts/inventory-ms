package com.celotts.productservice.infrastructure.adapter.input.rest.mapper;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductUpdateDTO;

import java.time.LocalDateTime;
import java.util.function.Consumer;

public class ProductModelUpdater {
    //TODO: Pendiente con esto
    public static void applyUpdate(ProductModel model, ProductUpdateDTO dto) {
        updateFieldIfNotNull(dto.getCode(), model::setCode);
        updateFieldIfNotNull(dto.getDescription(), model::setDescription);
        updateFieldIfNotNull(dto.getProductTypeCode(), model::setProductTypeCode);
        updateFieldIfNotNull(dto.getUnitCode(), model::setUnitCode);
        updateFieldIfNotNull(dto.getBrandId(), model::setBrandId);
        updateFieldIfNotNull(dto.getMinimumStock(), model::setMinimumStock);
        updateFieldIfNotNull(dto.getCurrentStock(), model::setCurrentStock);
        updateFieldIfNotNull(dto.getUnitPrice(), model::setUnitPrice);
        updateFieldIfNotNull(dto.getEnabled(), model::setEnabled);

        // Auditoría
        model.setUpdatedAt(LocalDateTime.now());
        model.setUpdatedBy(dto.getUpdatedBy() != null ? dto.getUpdatedBy() : "system");
    }

    private static <T> void updateFieldIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

}
