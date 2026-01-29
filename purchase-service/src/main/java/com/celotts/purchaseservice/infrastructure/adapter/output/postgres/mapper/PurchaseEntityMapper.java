package com.celotts.purchaseservice.infrastructure.adapter.output.postgres.mapper; // 👈 Cambiado 'purchese' por 'purchase'

import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import com.celotts.purchaseservice.infrastructure.adapter.output.postgres.entity.PurchaseEntity; // 👈 Import corregido a la subcarpeta purchase
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PurchaseEntityMapper {

    // Model -> Entity (Creación)
    PurchaseEntity toEntity(PurchaseModel model);

    // Entity -> Model (Lectura)
    PurchaseModel toModel(PurchaseEntity entity);

    // Update parcial (Actualización)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true)
    })
    void updateEntityFromModel(PurchaseModel model, @MappingTarget PurchaseEntity entity);

    // Mapeo de listas para Paginación
    List<PurchaseModel> toModelList(List<PurchaseEntity> entities);
}