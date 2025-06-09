package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.category;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryRequestDto;
import org.springframework.stereotype.Component;

@Component
public class CategoryRequestMapper {
    public CategoryModel toModel(CategoryRequestDto dto) {
        return CategoryModel.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .active(dto.getActive() != null ? dto.getActive() : Boolean.TRUE)
                .createdBy(dto.getCreatedBy() != null ? dto.getCreatedBy() : "system")
                .build();
    }

    public static void updateModelFromDto(CategoryModel model, CategoryRequestDto dto) {
        model.update(
                dto.getName(),
                dto.getDescription(),
                dto.getActive(),
                dto.getUpdatedBy()
        );
    }
}
