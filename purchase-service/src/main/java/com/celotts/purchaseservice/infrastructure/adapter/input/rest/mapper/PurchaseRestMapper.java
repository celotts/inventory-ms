package com.celotts.purchaseservice.infrastructure.adapter.input.rest.mapper;

import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.PurchaseRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseRestMapper {

    // Convierte lo que viene de la web al modelo de negocio
    @Mapping(target = "id", ignore = true)
    PurchaseModel toModel(PurchaseRequest request);

    // Convierte el modelo de negocio a una respuesta (opcional si usas el modelo como respuesta)
    PurchaseRequest toResponse(PurchaseModel model);
}