package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productTag;

import com.celotts.productservice.domain.model.ProductTagModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductTag.ProductTagCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductTag.ProductTagResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductTag.ProductTagUpdateDto;
import org.springframework.stereotype.Component;

@Component
public class ProductTagRequestMapper {

    public ProductTagModel toModel(ProductTagCreateDto dto) {
        if (dto == null) return null;
        return ProductTagModel.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .enabled(dto.getEnabled() != null ? dto.getEnabled() : Boolean.TRUE)
                .createdBy(dto.getCreatedBy())
                .build();
    }

    public ProductTagModel toModel(ProductTagUpdateDto dto) {
        if (dto == null) return null;
        return ProductTagModel.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .enabled(dto.getEnabled())
                .updatedBy(dto.getUpdatedBy())
                .build();
    }

    public ProductTagResponseDto toResponse(ProductTagModel m) {
        if (m == null) return null;
        return ProductTagResponseDto.builder()
                .id(m.getId())
                .name(m.getName())
                .description(m.getDescription())
                .enabled(m.getEnabled())
                .createdBy(m.getCreatedBy())
                .updatedBy(m.getUpdatedBy())
                .createdAt(m.getCreatedAt())
                .updatedAt(m.getUpdatedAt())
                .build();
    }
}