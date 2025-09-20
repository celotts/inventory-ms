package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.lot;

import com.celotts.productservice.domain.model.common.AuditModel;
import com.celotts.productservice.domain.model.lot.LotModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.lot.LotEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CentralMapperConfig;
import org.mapstruct.*;

@Mapper(config = CentralMapperConfig.class, imports = AuditModel.class)
public interface LotEntityMapper {

    @Mappings({
            @Mapping(target = "audit", expression = "java(new AuditModel(e.getCreatedAt(), e.getCreatedBy(), e.getUpdatedAt(), e.getUpdatedBy(), e.getDeletedAt(), e.getDeletedBy(), e.getDeletedReason()))")
    })
    LotModel toModel(LotEntity e);

    @InheritInverseConfiguration
    LotEntity toEntity(LotModel m);
}
