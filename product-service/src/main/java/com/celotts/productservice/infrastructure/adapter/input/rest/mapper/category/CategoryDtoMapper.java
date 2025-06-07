package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.category;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryRequestDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryUpdateDto;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class CategoryDtoMapper {

    public CategoryUpdateDto toUpdateDto(CategoryRequestDto requestDto) {
        return CategoryUpdateDto.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .active(requestDto.getActive())
                .updatedBy(requestDto.getUpdatedBy())
                .build();
    }

    public CategoryModel toModel(CategoryRequestDto requestDto) {
        return CategoryModel.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .active(requestDto.getActive() != null ? requestDto.getActive() : Boolean.TRUE)
                .createdBy(requestDto.getCreatedBy() != null ? requestDto.getCreatedBy() : "system")
                .build();
    }

    public CategoryResponseDto toResponseDto(CategoryModel model) {
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

    public List<CategoryResponseDto> toResponseDtoList(List<CategoryModel> models) {
        return models.stream()
                .map(CategoryDtoMapper::toResponseDto)  // ✅ Ahora es método estático
                .toList();
    }
}