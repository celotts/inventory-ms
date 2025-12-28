package com.celotts.purchaseservice.infrastructure.adapter.input.rest.mapper;

import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.PurchaseRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseRestMapper {

    @Mapping(target = "id", ignore = true)
    PurchaseModel toModel(PurchaseRequest request);

    PurchaseRequest toResponse(PurchaseModel model);
}