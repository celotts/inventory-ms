package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.category;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.category.CategoryEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryEntityMapper {

    public CategoryEntity toEntity(CategoryModel category) {
        if (category == null) return null;

        CategoryEntity entity = new CategoryEntity();
        entity.setId(category.getId());
        entity.setName(category.getName());
        entity.setDescription(category.getDescription());
        entity.setActive(category.getActive());
        entity.setCreatedBy(category.getCreatedBy());
        entity.setUpdatedBy(category.getUpdatedBy());
        entity.setCreatedAt(category.getCreatedAt());
        entity.setUpdatedAt(category.getUpdatedAt());

        return entity;
    }

    public CategoryModel toDomain(CategoryEntity categoryEntity) {
        if (categoryEntity == null) return null;

        return CategoryModel.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .description(categoryEntity.getDescription())
                .active(categoryEntity.getActive())
                .createdBy(categoryEntity.getCreatedBy())
                .updatedBy(categoryEntity.getUpdatedBy())
                .createdAt(categoryEntity.getCreatedAt())
                .updatedAt(categoryEntity.getUpdatedAt())
                .build();
    }

    public List<CategoryModel> toDomainList(List<CategoryEntity> categoryEntities) {
        return categoryEntities.stream()
                .map(this::toDomain)
                .toList();
    }

    public List<CategoryEntity> toEntityList(List<CategoryModel> categories) {
        return categories.stream()
                .map(this::toEntity)
                .toList();
    }
}