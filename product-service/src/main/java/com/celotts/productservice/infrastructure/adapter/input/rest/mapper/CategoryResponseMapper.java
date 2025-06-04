package com.celotts.productservice.infrastructure.adapter.input.rest.mapper;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.CategoryResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CategoryResponseMapper {
    public CategoryResponseDTO toDto(CategoryModel model) {
        return CategoryResponseDTO.builder()
                .id(model.id())
                .name(model.name())
                .description(model.description())
                .createdAt(model.createdAt())
                .build();
    }
}