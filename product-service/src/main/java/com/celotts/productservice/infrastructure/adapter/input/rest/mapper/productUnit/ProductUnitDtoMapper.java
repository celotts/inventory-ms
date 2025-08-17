package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productUnit;

import com.celotts.productservice.domain.model.product.ProductUnitModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitUpdateDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProductUnitDtoMapper {

    public ProductUnitModel toModel(ProductUnitCreateDto createDto) {
        if (createDto == null) return null;
        return ProductUnitModel.builder()
                .code(createDto.getCode())
                .name(createDto.getName())
                .description(createDto.getDescription())
                .symbol(createDto.getSymbol())
                .enabled(createDto.getEnabled())
                .createdBy(createDto.getCreatedBy())
                .updatedBy(createDto.getUpdatedBy())
                .build();
    }

    // PUT: aplica todos los campos requeridos del DTO sobre el modelo existente
    public void apply(ProductUnitModel target, ProductUnitUpdateDto dto) {
        if (target == null || dto == null) return;
        target.setName(dto.getName());
        target.setDescription(dto.getDescription());
        target.setSymbol(dto.getSymbol());
        target.setEnabled(dto.getEnabled());
        target.setUpdatedBy(dto.getUpdatedBy());
        // updatedAt que lo maneje tu auditoría/JPA
    }

    public ProductUnitResponseDto toResponseDto(ProductUnitModel model) {
        if (model == null) return null;
        return ProductUnitResponseDto.builder()
                .id(model.getId())
                .code(model.getCode())
                .name(model.getName())
                .description(model.getDescription())
                .symbol(model.getSymbol())
                .enabled(model.getEnabled())
                .createdBy(model.getCreatedBy())
                .updatedBy(model.getUpdatedBy())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    public List<ProductUnitResponseDto> toResponseDtoList(List<ProductUnitModel> models) {
        if (models == null) return List.of();
        return models.stream()
                .filter(Objects::nonNull)
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}