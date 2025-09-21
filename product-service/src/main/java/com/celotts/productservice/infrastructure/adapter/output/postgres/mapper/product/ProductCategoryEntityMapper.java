package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CentralMapperConfig;

import com.celotts.productservice.domain.model.product.ProductCategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productcategory.ProductCategoryResponseDto;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductCategoryEntity;
import org.mapstruct.*;

@Mapper(config = CentralMapperConfig.class)
public interface ProductCategoryEntityMapper {

    // Model -> Entity
    ProductCategoryEntity toEntity(ProductCategoryModel model);

    // Entity -> Model
    @InheritInverseConfiguration(name = "toEntity")
    ProductCategoryModel toModel(ProductCategoryEntity entity);

    // Entity -> Response DTO
    ProductCategoryResponseDto toResponseDto(ProductCategoryEntity entity);

    // Update parcial (ignora nulos; no pisa auditoría/ID)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "deletedAt", ignore = true),
            @Mapping(target = "deletedBy", ignore = true),
            @Mapping(target = "deletedReason", ignore = true)
    })
    void updateEntityFromModel(ProductCategoryModel src, @MappingTarget ProductCategoryEntity target);
}