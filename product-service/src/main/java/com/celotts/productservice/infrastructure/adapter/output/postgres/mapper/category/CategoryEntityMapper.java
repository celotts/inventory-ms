package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.category;

import com.celotts.productservice.domain.model.category.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.category.CategoryEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CentralMapperConfig;
import org.mapstruct.*;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface CategoryEntityMapper {

    // Model -> Entity
    CategoryEntity toEntity(CategoryModel model);

    // Entity -> Model
    CategoryModel toModel(CategoryEntity entity);

    // List<Entity> -> List<Model>
    List<CategoryModel> toModelList(List<CategoryEntity> entities);

    // (Opcional) List<Model> -> List<Entity>
    List<CategoryEntity> toEntityList(List<CategoryModel> models);

    // PATCH: no pisa ID ni campos de creación
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateEntityFromModel(CategoryModel src, @MappingTarget CategoryEntity target);
}