package com.celotts.supplierservice.infrastructure.adapter.output.postgres.mapper.supplier;

import com.celotts.supplierservice.domain.model.supplier.SupplierModel;
import com.celotts.supplierservice.infrastructure.adapter.input.rest.mapper.CentralMapperConfig;
import com.celotts.supplierservice.infrastructure.adapter.output.postgres.entity.supplier.SupplierEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface SupplierEntityMapper {

    // Model -> Entity
    SupplierEntity toEntity(SupplierModel model);

    // Entity -> Model   << NECESARIO para mapper::toModel
    SupplierModel toModel(SupplierEntity entity);

    // List<Entity> -> List<Model>
    List<SupplierModel> toModelList(List<SupplierEntity> entities);

    // List<Model> -> List<Entity>
    List<SupplierEntity> toEntityList(List<SupplierModel> models);

    // PATCH: no tocar ID, auditoría ni soft-delete
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "deletedReason", ignore = true)
    void updateEntityFromModel(SupplierModel src, @MappingTarget SupplierEntity target);
}