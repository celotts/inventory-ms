package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productImage;

import com.celotts.productservice.domain.model.product.ProductImageModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productImage.ProductImageCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productImage.ProductImageResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ProductImageDtoMapper {
    public ProductImageModel toModel(ProductImageCreateDto createDto) {
        if (createDto == null) return null;
        return ProductImageModel.builder()
                .productId(createDto.getProductId())
                .url(createDto.getUrl())
                .enabled(createDto.getEnabled())
                .build();

    }

    public ProductImageResponseDto toDto(ProductImageModel model) {
        if(model == null) return null;
        return ProductImageResponseDto.builder()
                .id(model.getId())
                .productId(model.getProductId())
                .url(model.getUrl())
                .uploadedAt(model.getUploadedAt())
                .enabled((model.getEnabled()))
                .createdAt(model.getCreatedAt())
                .updateAt(model.getUpdateAt())
                .updatedBy(model.getUpdatedBy())
                .createdBy(model.getCreatedBy())
                .build();

    }
}
