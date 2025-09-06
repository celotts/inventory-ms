package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productImage;

import com.celotts.productservice.domain.model.product.ProductImageModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productImage.ProductImageUpdateDto;
import com.celotts.productservice.infrastructure.common.util.MapperUtils;

import java.time.LocalDateTime;

public final class ProductImageModelUpdateMapper {

    private ProductImageModelUpdateMapper() {
        throw new UnsupportedOperationException("Utility class - instantiation not allowed");
    }

    public static void apply(ProductImageModel model, ProductImageUpdateDto dto) {
        if (model == null || dto == null) return;

        // Sin .getActive() (no existe en tu DTO)
        MapperUtils.updateFieldIfNotNull(dto.getUrl(), model::setUrl);
        MapperUtils.updateFieldIfNotNull(dto.getEnabled(), model::setEnabled);
        MapperUtils.updateFieldIfNotNull(dto.getUploadedAt(), model::setUploadedAt);
        MapperUtils.updateFieldIfNotNull(dto.getUpdatedBy(), v -> model.setUpdatedBy(v != null ? v.trim() : null));

        model.setUpdateAt(LocalDateTime.now());
    }
}