package com.celotts.productservice.infrastructure.adapter.input.rest.mapper;

import com.celotts.productservice.domain.model.ProductCategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductCategoryRequestDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductCategoryRequestMapper {
    public ProductCategoryModel toModel(ProductCategoryRequestDTO dto) {
        return new ProductCategoryModel(
                null,
                dto.getProductId(),
                dto.getCategoryId(),
                LocalDateTime.now()
        );
    }
}