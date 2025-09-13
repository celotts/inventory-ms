package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productunit;


import com.celotts.productservice.domain.model.product.ProductUnitModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitUpdateDto;


import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import org.springframework.data.domain.Page;

import java.util.List;
@Mapper(componentModel = "spring")
public interface ProductUnitMapper {

    // CREATE DTO -> MODEL
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    ProductUnitModel toModel(ProductUnitCreateDto dto);

    // UPDATE parcial (ignora nulls)
    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateModelFromDto(@MappingTarget ProductUnitModel target, ProductUnitUpdateDto dto);

    // MODEL -> RESPONSE
    ProductUnitResponseDto toResponse(ProductUnitModel model);
    List<ProductUnitResponseDto> toResponseList(List<ProductUnitModel> models);

    default Page<ProductUnitResponseDto> toResponse(Page<ProductUnitModel> page){
        return page.map(this::toResponse);
    }
}
