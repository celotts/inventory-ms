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
        return CategoryResponseDto.builder()
                .id(model.getId())
                .name(model.getName())
                .description(model.getDescription())
                .active(model.getActive())
                .build();
    }


}