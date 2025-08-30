package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.category;

import com.celotts.productservice.domain.model.category.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.category.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CategoryEntityMapper {

    // Model -> Entity
    CategoryEntity toEntity(CategoryModel model);

    // Entity -> Model
    @Mapping(source = "deleted", target = "deleted") // explícito si 'deleted' existe en ambos lados
    CategoryModel toModel(CategoryEntity entity);

    // List<Entity> -> List<Model>
    List<CategoryModel> toModelList(List<CategoryEntity> entities);
}