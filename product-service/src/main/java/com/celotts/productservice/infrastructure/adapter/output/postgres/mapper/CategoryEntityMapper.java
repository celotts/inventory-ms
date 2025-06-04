package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryEntityMapper {
    public CategoryModel toModel(Category entity) {
        return new CategoryModel(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt()
        );
    }

    public Category toEntity(CategoryModel model) {
        Category entity = new Category();
        entity.setId(model.id());
        entity.setName(model.name());
        entity.setDescription(model.description());
        entity.setCreatedAt(model.createdAt());
        return entity;
    }
}