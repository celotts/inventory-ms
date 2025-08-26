package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productTagAssignment;

import com.celotts.productservice.domain.model.product.ProductTagAssignmentModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTagAssignment.ProductTagAssignmentCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTagAssignment.ProductTagAssignmentUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTagAssignment.ProductTagAssignmentResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ProductTagAssignmentDtoMapper {

    public ProductTagAssignmentModel toModel(ProductTagAssignmentCreateDto dto) {
        if (dto == null) return null;
        return ProductTagAssignmentModel.builder()
                .productId(dto.getProductId())
                .tagId(dto.getTagId())
                .enabled(dto.getEnabled() != null ? dto.getEnabled() : Boolean.TRUE)
                .createdBy(dto.getCreatedBy())
                .build();
    }

    public ProductTagAssignmentModel toModel(ProductTagAssignmentUpdateDto dto) {
        if (dto == null) return null;
        return ProductTagAssignmentModel.builder()
                .enabled(dto.getEnabled())
                .updatedBy(dto.getUpdatedBy())   // <- ahora existe el getter
                .build();
    }

    public ProductTagAssignmentResponseDto toResponseDto(ProductTagAssignmentModel m) {
        if (m == null) return null;
        return ProductTagAssignmentResponseDto.builder()
                .id(m.getId())
                .productId(m.getProductId())
                .tagId(m.getTagId())
                .enabled(m.getEnabled())
                .createdBy(m.getCreatedBy())
                .updatedBy(m.getUpdatedBy())
                .createdAt(m.getCreatedAt())
                .updatedAt(m.getUpdatedAt())
                .build();
    }
}