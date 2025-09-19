package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CentralMapperConfig;

import com.celotts.productservice.domain.model.product.ProductModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductEntity;
import org.mapstruct.*;

@Mapper(config = CentralMapperConfig.class)
public interface ProductEntityMapper {

    // Model -> Entity
    ProductEntity toEntity(ProductModel model);

    // Entity -> Model
    @InheritInverseConfiguration(name = "toEntity")
    ProductModel toModel(ProductEntity entity);

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
    void updateEntityFromModel(ProductModel src, @MappingTarget ProductEntity target);
}