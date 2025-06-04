package com.celotts.productservice.infrastructure.adapter.input.rest.mapper;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductUpdateDTO;
import static com.celotts.productservice.infrastructure.common.util.MapperUtils.updateFieldIfNotNull;

import java.time.LocalDateTime;


public class ProductModelUpdater {
    public static void applyUpdate(ProductModel model, ProductUpdateDTO dto) {
        ProductModelUpdateMapper.apply(model, dto);
    }
}
