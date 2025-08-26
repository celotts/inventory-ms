package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CentralMapperConfig;
import com.celotts.productservice.domain.model.product.ProductTagAssignmentModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTagAssignmentEntity;
import org.mapstruct.*;

@Mapper(config = CentralMapperConfig.class)
public interface ProductTagAssignmentEntityMapper {

    // Model -> Entity
    ProductTagAssignmentEntity toEntity(ProductTagAssignmentModel model);

    // Entity -> Model
    @InheritInverseConfiguration(name = "toEntity")
    ProductTagAssignmentModel toModel(ProductTagAssignmentEntity entity);

    // Update parcial (ignora nulos; no pisa auditoría/ID)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true)
    })
    void updateEntityFromModel(ProductTagAssignmentModel src, @MappingTarget ProductTagAssignmentEntity target);
}