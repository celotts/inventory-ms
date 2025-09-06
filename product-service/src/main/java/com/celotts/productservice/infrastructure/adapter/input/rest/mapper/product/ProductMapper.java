package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product;

import com.celotts.productservice.domain.model.product.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // CREATE DTO -> MODEL
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    ProductModel toModel(ProductCreateDto dto);

    // UPDATE parcial (si lo usas en Service)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateModelFromDto(@MappingTarget ProductModel target, ProductUpdateDto dto);

    // (Opcional) UPDATE DTO -> MODEL (si tu UseCase recibe model desde updateDto)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    ProductModel toModel(ProductUpdateDto dto);

    // MODEL -> RESPONSE
    ProductResponseDto toResponse(ProductModel model);

    // LISTAS
    List<ProductResponseDto> toResponseList(List<ProductModel> models);
}