package com.celotts.salesservice.infrastructure.adapter.input.rest.mapper;

import com.celotts.salesservice.domain.model.SaleItemModel;
import com.celotts.salesservice.domain.model.SaleModel;
import com.celotts.salesservice.infrastructure.adapter.input.rest.dto.SaleCreateDto;
import com.celotts.salesservice.infrastructure.adapter.input.rest.dto.SaleItemCreateDto;
import com.celotts.salesservice.infrastructure.adapter.output.persistence.entity.SaleEntity;
import com.celotts.salesservice.infrastructure.adapter.output.persistence.entity.SaleItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    // DTO -> Model
    SaleModel toModel(SaleCreateDto dto);
    SaleItemModel toItemModel(SaleItemCreateDto dto);

    // Model -> Entity
    @Mapping(target = "items", source = "items")
    SaleEntity toEntity(SaleModel model);
    
    @Mapping(target = "sale", ignore = true)
    SaleItemEntity toItemEntity(SaleItemModel model);

    // Entity -> Model
    SaleModel toModel(SaleEntity entity);
    List<SaleModel> toModelList(List<SaleEntity> entities);
}