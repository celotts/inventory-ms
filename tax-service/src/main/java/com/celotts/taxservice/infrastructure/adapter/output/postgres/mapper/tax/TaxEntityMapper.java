package com.celotts.taxservice.infrastructure.adapter.output.postgres.mapper.tax;

import com.celotts.taxservice.domain.model.TaxModel;
import com.celotts.taxservice.infrastructure.adapter.output.postgres.entity.TaxEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class TaxEntityMapper {

    public TaxModel toModel(TaxEntity entity) {
        if (entity == null) {
            return null;
        }

        return TaxModel.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .rate(entity.getRate())
                .validFrom(entity.getValidFrom())
                .validTo(entity.getValidTo())
                .isActive(entity.getIsActive())
                .createdAt(localDateTimeToOffsetDateTime(entity.getCreatedAt()))
                .updatedAt(localDateTimeToOffsetDateTime(entity.getUpdatedAt()))
                .build();
    }

    public TaxModel toDomain(TaxEntity entity) {
        return toModel(entity);
    }

    public TaxEntity toEntity(TaxModel model) {
        if (model == null) {
            return null;
        }

        return TaxEntity.builder()
                .id(model.getId())
                .code(model.getCode())
                .name(model.getName())
                .rate(model.getRate())
                .validFrom(model.getValidFrom())
                .validTo(model.getValidTo())
                .isActive(model.getIsActive())
                // NO incluir createdAt y updatedAt - se generan automáticamente en la BD
                .build();
    }

    private OffsetDateTime localDateTimeToOffsetDateTime(LocalDateTime value) {
        if (value == null) {
            return null;
        }
        return value.atOffset(ZoneOffset.UTC);
    }

    private LocalDateTime offsetDateTimeToLocalDateTime(OffsetDateTime value) {
        if (value == null) {
            return null;
        }
        return value.toLocalDateTime();
    }
}