package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.domain.model.product.ProductBrandModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductBrandEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductBrandEntityMapper {

    ProductBrandEntity toEntity(ProductBrandModel model);

    @InheritInverseConfiguration(name = "toEntity")
    ProductBrandModel toModel(ProductBrandEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "createdAt", ignore = true),
        @Mapping(target = "createdBy", ignore = true)
    })
    void updateEntityFromModel(ProductBrandModel src, @MappingTarget ProductBrandEntity target);
}