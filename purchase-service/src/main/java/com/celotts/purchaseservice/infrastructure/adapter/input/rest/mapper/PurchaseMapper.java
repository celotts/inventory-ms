package com.celotts.purchaseservice.infrastructure.adapter.input.rest.mapper;

import com.celotts.purchaseservice.domain.model.purchase.PurchaseItemModel;
import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.PurchaseCreateDto;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.PurchaseItemCreateDto;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.PurchaseItemResponseDto;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.PurchaseResponseDto;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.PurchaseUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {
    PurchaseModel toModel(PurchaseCreateDto dto);
    PurchaseModel toModel(PurchaseUpdateDto dto);
    PurchaseResponseDto toResponse(PurchaseModel model);

    // Mapeo para la lista de items
    List<PurchaseItemModel> toItemModelList(List<PurchaseItemCreateDto> dtos);
    PurchaseItemModel toItemModel(PurchaseItemCreateDto dto);

    // Mapeo para la lista de items de respuesta
    List<PurchaseItemResponseDto> toItemResponseList(List<PurchaseItemModel> models);
    PurchaseItemResponseDto toItemResponse(PurchaseItemModel model);
}
