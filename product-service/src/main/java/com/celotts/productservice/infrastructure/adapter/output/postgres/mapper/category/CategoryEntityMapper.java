package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.category;

import com.celotts.productservice.domain.model.category.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.category.CategoryEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface CategoryEntityMapper {

    @Mapping(source = "createdBy", target = "createdBy")
    CategoryEntity toEntity(CategoryModel model);

    @Mapping(source = "createdBy", target = "createdBy")
    CategoryModel toModel(CategoryEntity entity);

    List<CategoryModel> toModelList(List<CategoryEntity> entities);
}
