package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CentralMapperConfig;
import com.celotts.productservice.domain.model.product.ProductBrandModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductBrandEntity;
import org.mapstruct.*;

@Mapper(config = CentralMapperConfig.class)
public interface ProductBrandEntityMapper {

    @Mapping(source = "createdBy", target = "createdBy")
    ProductBrandEntity toEntity(ProductBrandModel model);

    @InheritInverseConfiguration(name = "toEntity")
    ProductBrandModel toModel(ProductBrandEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "deletedAt", ignore = true),
            @Mapping(target = "deletedBy", ignore = true),
            @Mapping(target = "deletedReason", ignore = true),
            @Mapping(target = "enabled", ignore = true)
    })
    void updateEntityFromModel(ProductBrandModel src, @MappingTarget ProductBrandEntity target);
}
