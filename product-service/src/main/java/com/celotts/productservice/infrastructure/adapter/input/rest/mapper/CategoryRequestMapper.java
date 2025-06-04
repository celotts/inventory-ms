package com.celotts.productservice.infrastructure.adapter.input.rest.mapper;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.CategoryRequestDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CategoryRequestMapper {
    public CategoryModel toModel(CategoryRequestDTO dto) {
        return new CategoryModel(
                null,
                dto.getName(),
                dto.getDescription(),
                LocalDateTime.now()
        );
    }
}