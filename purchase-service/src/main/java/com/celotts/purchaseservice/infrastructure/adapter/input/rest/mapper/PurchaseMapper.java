package com.celotts.purchaseservice.infrastructure.adapter.input.rest.mapper;

import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.PurchaseCreateDto;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.PurchaseResponseDto;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.PurchaseUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {
    PurchaseModel toModel(PurchaseCreateDto dto);
    PurchaseModel toModel(PurchaseUpdateDto dto);
    PurchaseResponseDto toResponse(PurchaseModel model);
}
