package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productImage;

import com.celotts.productservice.domain.model.product.ProductImageModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productImage.ProductImageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProductImageResponseMapper {

    public ProductImageResponseDto toDto(ProductImageModel model) {
        if (model == null) return null;

        return ProductImageResponseDto.builder()
                .id(model.getId())
                .productId(model.getProductId())
                .url(model.getUrl())
                .uploadedAt(model.getUploadedAt())
                .enabled(model.getEnabled())
                .createdAt(model.getCreatedAt())
                .createdBy(model.getCreatedBy())
                .updatedBy(model.getUpdatedBy())
                .updateAt(model.getUpdateAt())
                .build();
    }

    public List<ProductImageResponseDto> toDtoList(List<ProductImageModel> models) {
        if (models == null || models.isEmpty()) return Collections.emptyList();
        return models.stream()
                .filter(Objects::nonNull)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Page<ProductImageResponseDto> toDtoPage(Page<ProductImageModel> page) {
        if (page == null) return Page.empty();
        List<ProductImageResponseDto> content = toDtoList(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }
}