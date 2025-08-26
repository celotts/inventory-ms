package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CentralMapperConfig;
import com.celotts.productservice.domain.model.product.ProductTagAssignmentModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTagAssignmentEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductTagAssignmentEntityMapper {
    ProductTagAssignmentEntity toEntity(ProductTagAssignmentModel model);

    @InheritInverseConfiguration(name = "toEntity")
    ProductTagAssignmentModel toModel(ProductTagAssignmentEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true)
    })
    void updateEntityFromModel(ProductTagAssignmentModel src, @MappingTarget ProductTagAssignmentEntity target);
}