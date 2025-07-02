package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.category;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryResponseMapper {

    private CategoryResponseMapper() {
        // Clase utilitaria, no instanciar
    }

    /**
     * Convierte CategoryModel a CategoryResponseDto
     */
    public static CategoryResponseDto toDto(CategoryModel model) {
        return CategoryDtoMapper.toResponseDto(model);
    }

    /**
     * Convierte lista de CategoryModel a lista de CategoryResponseDto
     */
    //TODO: NO SE USA
    public static List<CategoryResponseDto> toDtoList(List<CategoryModel> models) {
        if (models == null) {
            return null;
        }

        return models.stream()
                .map(CategoryResponseMapper::toDto)  // ← Ahora SÍ funciona
                .collect(Collectors.toList());
    }
}