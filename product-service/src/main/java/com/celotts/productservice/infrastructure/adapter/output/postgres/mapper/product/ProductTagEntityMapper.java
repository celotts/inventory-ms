package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CentralMapperConfig;
import com.celotts.productservice.domain.model.product.ProductTagModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTagEntity;
import org.mapstruct.*;

@Mapper(config = CentralMapperConfig.class)
public interface ProductTagEntityMapper {

    ProductTagEntity toEntity(ProductTagModel model);

    @InheritInverseConfiguration(name = "toEntity")
    ProductTagModel toModel(ProductTagEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true)
    })
    void updateEntityFromModel(ProductTagModel src, @MappingTarget ProductTagEntity target);
}