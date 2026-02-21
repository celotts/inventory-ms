package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CentralMapperConfig;
import com.celotts.productservice.domain.model.product.ProductCategoryModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductCategoryEntity;
import org.mapstruct.*;

@Mapper(config = CentralMapperConfig.class)
public interface ProductCategoryEntityMapper {

    ProductCategoryEntity toEntity(ProductCategoryModel model);

    ProductCategoryModel toModel(ProductCategoryEntity entity);

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
    void updateEntityFromModel(ProductCategoryModel src, @MappingTarget ProductCategoryEntity target);
}
