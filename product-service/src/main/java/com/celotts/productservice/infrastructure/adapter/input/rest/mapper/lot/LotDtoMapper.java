package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.lot;

import com.celotts.productservice.domain.model.lot.LotModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.lot.LotCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.lot.LotResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.lot.LotUpdateDto;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper(config = CentralMapperConfig.class)
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
            @Mapping(target = "stage", ignore = true),
            @Mapping(target = "enabled", constant = "true"),
            @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "createdBy", constant = "api"),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "deletedAt", ignore = true),
            @Mapping(target = "deletedBy", ignore = true),
            @Mapping(target = "deletedReason", ignore = true),
            @Mapping(target = "receivedAt", ignore = true)
    })
    LotModel toModel(LotCreateDto dto);

    // UPDATE
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
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "deletedAt", ignore = true),
            @Mapping(target = "deletedBy", ignore = true),
            @Mapping(target = "deletedReason", ignore = true),
            @Mapping(target = "receivedAt", ignore = true)
    })
    LotModel toModel(LotUpdateDto dto);

    // MODEL -> RESPONSE
    @Mappings({
            @Mapping(target = "expired", expression = "java(isExpired(model))"),
            @Mapping(target = "disposed", expression = "java(isDisposed(model))")
    })
    LotResponseDto toResponse(LotModel model);

    default boolean isExpired(LotModel model) {
        LocalDate exp = model.getExpirationDate();
        return exp != null && !exp.isAfter(LocalDate.now());
    }

    default boolean isDisposed(LotModel model) {
        return model.getStage() != null && "DISPOSED".equalsIgnoreCase(model.getStage().name());
    }
}
