package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CentralMapperConfig;
import com.celotts.productservice.domain.model.product.ProductUnitModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductUnitEntity;
import org.mapstruct.*;

@Mapper(config = CentralMapperConfig.class)
public interface ProductUnitEntityMapper {

    // Model -> Entity
    ProductUnitEntity toEntity(ProductUnitModel model);

    // Entity -> Model
    @InheritInverseConfiguration(name = "toEntity")
    ProductUnitModel toModel(ProductUnitEntity entity);

    // Update parcial (ignora nulos; no pisa auditoría/ID)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true)
    })
    void updateEntityFromModel(ProductUnitModel src, @MappingTarget ProductUnitEntity target);
}