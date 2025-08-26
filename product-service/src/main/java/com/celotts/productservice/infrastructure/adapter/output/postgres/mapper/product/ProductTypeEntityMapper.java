package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CentralMapperConfig;
import com.celotts.productservice.domain.model.product.ProductTypeModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = CentralMapperConfig.class)
public interface ProductTypeEntityMapper {

    // Model -> Entity
    ProductTypeEntity toEntity(ProductTypeModel model);

    // Entity -> Model
    @InheritInverseConfiguration(name = "toEntity")
    ProductTypeModel toModel(ProductTypeEntity entity);

    // Update parcial (ignora nulos; no pisa auditoría/ID)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "createdAt", ignore = true),
        @Mapping(target = "createdBy", ignore = true)
    })
    void updateEntityFromModel(ProductTypeModel src, @MappingTarget ProductTypeEntity target);
}