package com.celotts.supplierservice.infrastructure.adapter.output.postgres.mapper.supplier;

import com.celotts.supplierservice.domain.model.supplier.SupplierModel;
import com.celotts.supplierservice.infrastructure.adapter.input.rest.mapper.CentralMapperConfig;
import com.celotts.supplierservice.infrastructure.adapter.output.postgres.entity.supplier.SupplierEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface SupplierEntityMapper {

    // Model -> Entity
    // Añadimos los ignores aquí para que MapStruct no se queje de los campos técnicos
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "deletedReason", ignore = true)
    SupplierEntity toEntity(SupplierModel model);

    // Entity -> Model
    SupplierModel toModel(SupplierEntity entity);

    // Listas
    List<SupplierModel> toModelList(List<SupplierEntity> entities);
    List<SupplierEntity> toEntityList(List<SupplierModel> models);

    // PATCH
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