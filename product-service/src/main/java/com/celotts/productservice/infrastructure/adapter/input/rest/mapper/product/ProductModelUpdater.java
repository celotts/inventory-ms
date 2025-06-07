package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductUpdateDTO;

//TODO: not used method
public class ProductModelUpdater {
    public static void applyUpdate(ProductModel model, ProductUpdateDTO dto) {
        ProductModelUpdateMapper.apply(model, dto);
    }
}
