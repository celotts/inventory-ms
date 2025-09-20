package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.lot;

import com.celotts.productservice.domain.model.common.AuditModel;
import com.celotts.productservice.domain.model.lot.LotModel;
import com.celotts.productservice.domain.model.lot.LotStage;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.lot.LotCreateDto;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CentralMapperConfig;
import java.time.Instant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = CentralMapperConfig.class, imports = {AuditModel.class, LotStage.class, Instant.class})
public interface LotDtoMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "stage", expression = "java(LotStage.AVAILABLE)"),
            @Mapping(target = "supplierId", ignore = true),
            @Mapping(target = "enabled", constant = "true"),
            @Mapping(target = "audit", expression = "java(new AuditModel(Instant.now(), \"api\", null, null, null, null, null))")
    })
    LotModel toModel(LotCreateDto dto);
}