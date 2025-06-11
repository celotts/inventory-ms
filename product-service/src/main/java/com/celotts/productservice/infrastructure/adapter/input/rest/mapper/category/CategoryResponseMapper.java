package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.category;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryResponseMapper {

    public CategoryResponseDto toDto(CategoryModel model) {
        if (model == null) return null;

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

    public List<CategoryResponseDto> toDtoList(List<CategoryModel> models) {
        if (models == null) return null;

        return models.stream()
                .map(this::toDto)
                .toList();
    }
}