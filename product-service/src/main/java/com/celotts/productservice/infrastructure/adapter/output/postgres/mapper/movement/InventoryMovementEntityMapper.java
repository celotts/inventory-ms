package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.movement;

import com.celotts.productservice.domain.model.common.AuditLite;
import com.celotts.productservice.domain.model.movement.InventoryMovementModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.movement.InventoryMovementEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CentralMapperConfig;
import org.mapstruct.*;

@Mapper(config = CentralMapperConfig.class, imports = AuditLite.class)
public interface InventoryMovementEntityMapper {

    @Mappings({
            @Mapping(target = "audit", expression = "java(new AuditLite(e.getCreatedAt(), e.getCreatedBy(), e.getDeletedAt(), e.getDeletedBy()))")
    })
    InventoryMovementModel toModel(InventoryMovementEntity e);

    @InheritInverseConfiguration
    InventoryMovementEntity toEntity(InventoryMovementModel m);
}