package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.domain.model.product.ProductModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
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
        @Mapping(target = "createdBy", ignore = true)
    })
    void updateEntityFromModel(ProductModel src, @MappingTarget ProductEntity target);
}