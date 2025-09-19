package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CentralMapperConfig;
import com.celotts.productservice.domain.model.product.ProductUnitModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductUnitEntity;
import org.mapstruct.*;

@Mapper(config = CentralMapperConfig.class)
public interface ProductUnitEntityMapper {

    ProductUnitEntity toEntity(ProductUnitModel model);

    @InheritInverseConfiguration(name = "toEntity")
    ProductUnitModel toModel(ProductUnitEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),

            // auditoría/soft-delete: los maneja la BD / listeners
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "deletedAt", ignore = true),
            @Mapping(target = "deletedBy", ignore = true),
            @Mapping(target = "deletedReason", ignore = true),
            @Mapping(target = "enabled", ignore = true)
    })
    void updateEntityFromModel(ProductUnitModel src, @MappingTarget ProductUnitEntity target);
}