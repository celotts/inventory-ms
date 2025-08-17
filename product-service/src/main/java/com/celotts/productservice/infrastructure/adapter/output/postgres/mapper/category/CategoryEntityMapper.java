package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.category;

import com.celotts.productservice.domain.model.category.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.category.CategoryEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryEntityMapper {

    public CategoryEntity toEntity(CategoryModel model) {
        if (model == null) return null;
        CategoryEntity e = new CategoryEntity();
        e.setId(model.getId());
        e.setName(model.getName());
        e.setDescription(model.getDescription());
        e.setActive(model.getActive());
        e.setCreatedAt(model.getCreatedAt());
        e.setUpdatedAt(model.getUpdatedAt());
        e.setCreatedBy(model.getCreatedBy());
        e.setUpdatedBy(model.getUpdatedBy());
        return e;
    }

    public CategoryModel toModel(CategoryEntity e) {
        if (e == null) return null;
        CategoryModel m = new CategoryModel();
        m.setId(e.getId());
        m.setName(e.getName());
        m.setDescription(e.getDescription());
        m.setActive(e.getActive());
        m.setDeleted(e.getDeleted());
        m.setCreatedAt(e.getCreatedAt());
        m.setUpdatedAt(e.getUpdatedAt());
        m.setCreatedBy(e.getCreatedBy());
        m.setUpdatedBy(e.getUpdatedBy());
        return m;
    }

    public List<CategoryModel> toModelList(List<CategoryEntity> entities) {
        return entities.stream().map(this::toModel).collect(Collectors.toList());
    }
}