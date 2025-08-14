package com.celotts.productserviceOld.infrastructure.adapter.input.rest.mapper.category;

import com.celotts.productserviceOld.domain.model.CategoryModel;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.category.CategoryCreateDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.category.CategoryUpdateDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.category.CategoryResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryDtoMapper {

    /**
     * Convierte CategoryCreateDto a CategoryModel
     */
    public static CategoryModel toModelFromCreate(CategoryCreateDto dto) {
        if (dto == null) {
            return null;
        }

        return CategoryModel.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .active(true)
                .createdBy("system") // Esto debería venir del contexto de seguridad
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * Convierte CategoryUpdateDto a CategoryModel
     */
    public static CategoryModel toModelFromUpdate(CategoryUpdateDto dto) {
        if (dto == null) {
            return null;
        }

        return CategoryModel.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .active(dto.getActive())
                .updatedBy(dto.getUpdatedBy() != null ? dto.getUpdatedBy() : "system")
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Convierte CategoryModel a CategoryResponseDto
     */
    public static CategoryResponseDto toResponseDto(CategoryModel model) {
        if (model == null) {
            return null;
        }

        return CategoryResponseDto.builder()
                .id(model.getId())
                .name(model.getName())
                .description(model.getDescription())
                .active(model.getActive())
                .createdBy(model.getCreatedBy())
                .updatedBy(model.getUpdatedBy())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    /**
     * Convierte lista de CategoryModel a lista de CategoryResponseDto
     */
    public static List<CategoryResponseDto> toResponseDtoList(List<CategoryModel> models) {
        if (models == null) {
            return null;
        }

        return models.stream()
                .map(CategoryDtoMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Método de compatibilidad con el código existente
     * @deprecated Usar toModelFromCreate o toModelFromUpdate según corresponda
     */
    @Deprecated
    public static CategoryModel toModel(Object dto) {
        if (dto instanceof CategoryCreateDto) {
            return toModelFromCreate((CategoryCreateDto) dto);
        } else if (dto instanceof CategoryUpdateDto) {
            return toModelFromUpdate((CategoryUpdateDto) dto);
        }
        throw new IllegalArgumentException("Unsupported DTO type: " + dto.getClass().getSimpleName());
    }
}