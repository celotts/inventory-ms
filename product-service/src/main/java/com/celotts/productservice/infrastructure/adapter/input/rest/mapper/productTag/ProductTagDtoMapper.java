package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productTag;

import com.celotts.productservice.domain.model.ProductTagModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductTag.*;
import org.springframework.stereotype.Component;

@Component
public class ProductTagDtoMapper {

    // CreateDto -> Model
    public ProductTagModel toModel(ProductTagCreateDto dto) {
        if (dto == null) return null;
        return ProductTagModel.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .enabled(dto.getEnabled() != null ? dto.getEnabled() : Boolean.TRUE)
                .createdBy(dto.getCreatedBy())
                .build();
    }

    // UpdateDto -> Model (solo campos editables)
    public ProductTagModel toModel(ProductTagUpdateDto dto) {
        if (dto == null) return null;
        return ProductTagModel.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .enabled(dto.getEnabled())
                .updatedBy(dto.getUpdatedBy())
                .build();
    }

    // Merge UpdateDto sobre un modelo existente (si prefieres actualizar en controller/servicio)
    public ProductTagModel updateModelFromDto(ProductTagModel current, ProductTagUpdateDto dto) {
        if (current == null) return null;
        if (dto == null) return current;

        return current.toBuilder() // asegúrate que ProductTagModel tenga @Builder(toBuilder = true)
                .name(dto.getName() != null ? dto.getName() : current.getName())
                .description(dto.getDescription() != null ? dto.getDescription() : current.getDescription())
                .enabled(dto.getEnabled() != null ? dto.getEnabled() : current.getEnabled())
                .updatedBy(dto.getUpdatedBy() != null ? dto.getUpdatedBy() : current.getUpdatedBy())
                .build();
    }

    // Model -> ResponseDto
    public ProductTagResponseDto toResponseDto(ProductTagModel m) {
        if (m == null) return null;
        return ProductTagResponseDto.builder()
                .id(m.getId())
                .name(m.getName())
                .description(m.getDescription())
                .enabled(m.getEnabled())
                .createdBy(m.getCreatedBy())
                .updatedBy(m.getUpdatedBy())
                .build();
    }
}