package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productTagAssignment;

import com.celotts.productservice.domain.model.product.ProductTagAssignmentModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTagAssignment.ProductTagAssignmentCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTagAssignment.ProductTagAssignmentResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTagAssignment.ProductTagAssignmentUpdateDto;
import org.springframework.stereotype.Component;

@Component
public class ProductTagAssignmentRequestMapper {

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
                // si tu UpdateDto NO trae id/productId/tagId, no los pongas
                .enabled(dto.getEnabled())
                .updatedBy(dto.getUpdatedBy()) // ojo: getter correcto
                .build();
    }

    public ProductTagAssignmentResponseDto toResponse(ProductTagAssignmentModel m) {
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
                // .assignedAt(m.getAssignedAt()) // <- QUITADO porque el builder no lo tiene
                .build();
    }
}