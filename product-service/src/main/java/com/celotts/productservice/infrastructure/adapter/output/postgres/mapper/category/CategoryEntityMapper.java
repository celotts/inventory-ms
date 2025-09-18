package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.category;

import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CentralMapperConfig;

import com.celotts.productservice.domain.model.category.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.category.CategoryEntity;
import org.mapstruct.*;

import java.util.List;


@Mapper(config = CentralMapperConfig.class)
public interface CategoryEntityMapper {

    // Model -> Entity
    CategoryEntity toEntity(CategoryModel model);

    // Entity -> Model
    @Mapping(source = "deleted", target = "deleted") // explícito si 'deleted' existe en ambos lados
    CategoryModel toModel(CategoryEntity entity);

    // List<Entity> -> List<Model>
    List<CategoryModel> toModelList(List<CategoryEntity> entities);
}