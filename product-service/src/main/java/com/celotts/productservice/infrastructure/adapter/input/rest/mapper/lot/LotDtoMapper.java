package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.lot;

import com.celotts.productservice.domain.model.common.AuditModel;
import com.celotts.productservice.domain.model.lot.LotModel;
// NO importes LotStage aquí
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.lot.LotCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.lot.LotUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.lot.LotResponseDto;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.time.Instant;
import java.time.LocalDate;

@Mapper(
        config = CentralMapperConfig.class,
        imports = {AuditModel.class, Instant.class} // ← quitamos LotStage
)
public interface LotDtoMapper {

    // CREATE
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "productId", source = "productId"),
            @Mapping(target = "lotCode", source = "lotCode"),
            @Mapping(target = "quantity", source = "quantity"),
            @Mapping(target = "unitCost", source = "unitCost"),
            @Mapping(target = "mfgDate", source = "mfgDate"),
            @Mapping(target = "expirationDate", source = "expirationDate"),
            @Mapping(target = "notes", source = "notes"),
            @Mapping(target = "stage", ignore = true), // inicialízalo en dominio si quieres AVAILABLE
            @Mapping(target = "enabled", constant = "true"),
            @Mapping(target = "audit", expression = "java(new AuditModel(Instant.now(), \"api\", null, null, null, null, null))")
    })
    LotModel toModel(LotCreateDto dto);

    // UPDATE (PUT total)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "productId", ignore = true),
            @Mapping(target = "lotCode", source = "lotCode"),
            @Mapping(target = "quantity", source = "quantity"),
            @Mapping(target = "unitCost", source = "unitCost"),
            @Mapping(target = "mfgDate", source = "mfgDate"),
            @Mapping(target = "expirationDate", source = "expirationDate"),
            @Mapping(target = "notes", source = "notes"),
            @Mapping(target = "stage", ignore = true),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(target = "audit", ignore = true)
    })
    LotModel toModel(LotUpdateDto dto);

    // MODEL -> RESPONSE
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "productId", source = "productId"),
            @Mapping(target = "lotCode", source = "lotCode"),
            @Mapping(target = "quantity", source = "quantity"),
            @Mapping(target = "unitCost", source = "unitCost"),
            @Mapping(target = "mfgDate", source = "mfgDate"),
            @Mapping(target = "expirationDate", source = "expirationDate"),
            @Mapping(target = "notes", source = "notes"),
            // Usamos helpers para no depender de nombres exactos del enum:
            @Mapping(target = "expired",  expression = "java(isExpired(model))"),
            @Mapping(target = "disposed", expression = "java(isDisposed(model))")
    })
    LotResponseDto toResponse(LotModel model);

    // ===== Helpers =====
    default boolean isExpired(LotModel model) {
        // Regla por fecha (ajústala si tu dominio define otra cosa)
        LocalDate exp = model.expirationDate();
        return exp != null && !exp.isAfter(LocalDate.now());
    }

    default boolean isDisposed(LotModel model) {
        // Detecta por stage.name() sin atar a constantes específicas
        return model.stage() != null && "DISPOSED".equalsIgnoreCase(model.stage().name());
        // Alternativa si usas soft delete:
        // return model.audit() != null && model.audit().deletedAt() != null;
    }
}