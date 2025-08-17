package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.category;

import com.celotts.productservice.domain.model.category.CategoryModel;
import org.springframework.stereotype.Component;

@Component
public class CategoryRequestMapper {
    public CategoryModel toModel(CategoryModel dto) {
        return CategoryModel.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .active(dto.getActive() != null ? dto.getActive() : Boolean.TRUE)
                .createdBy(dto.getCreatedBy() != null ? dto.getCreatedBy() : "system")
                .build();
    }

    public static void updateModelFromDto(CategoryModel model, CategoryModel dto) {
        model.update(
                dto.getName(),
                dto.getDescription(),
                dto.getActive(),
                dto.getUpdatedBy()
        );
    }
}
