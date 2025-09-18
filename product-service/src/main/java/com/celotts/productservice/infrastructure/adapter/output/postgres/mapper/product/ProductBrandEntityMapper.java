package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CentralMapperConfig;

import com.celotts.productservice.domain.model.product.ProductBrandModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductBrandEntity;
import org.mapstruct.*;

@Mapper(config = CentralMapperConfig.class)
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