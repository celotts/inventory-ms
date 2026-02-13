package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.lot;

import com.celotts.productservice.domain.model.lot.LotModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.lot.LotEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CentralMapperConfig;
import org.mapstruct.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(config = CentralMapperConfig.class)
public interface LotEntityMapper {

    LotModel toModel(LotEntity e);

    @InheritInverseConfiguration
    LotEntity toEntity(LotModel m);

    // Métodos de conversión de tiempo
    default LocalDateTime map(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    default Instant map(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
}
