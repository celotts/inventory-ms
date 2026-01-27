package com.celotts.purchaseservice.infrastructure.adapter.output.postgres.mapper; // 👈 Cambiado 'purchese' por 'purchase'

import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import com.celotts.purchaseservice.infrastructure.adapter.output.postgres.entity.PurchaseEntity; // 👈 Import corregido a la subcarpeta purchase
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PurchaseEntityMapper {

    PurchaseModel toModel(PurchaseEntity entity);

    @Mapping(target = "createdAt", ignore = true) // La DB se encarga en @PrePersist
    @Mapping(target = "updatedAt", ignore = true) // La DB se encarga en @PreUpdate
    @Mapping(target = "createdBy", source = "createdBy") // Forzamos el mapeo por claridad
    PurchaseEntity toEntity(PurchaseModel model);

    // AGREGADO: Necesario para que el findAll con paginación funcione sin errores
    List<PurchaseModel> toModelList(List<PurchaseEntity> entities);
}