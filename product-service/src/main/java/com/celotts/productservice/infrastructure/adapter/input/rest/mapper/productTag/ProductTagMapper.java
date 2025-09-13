package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productTag;

import com.celotts.productservice.domain.model.product.ProductTagModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTag.ProductTagCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTag.ProductTagResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTag.ProductTagUpdateDto;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductTagMapper {

    // CREATE DTO -> MODEL
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    ProductTagModel toModel(ProductTagCreateDto dto);

    //UPDATE parcial (ignora nulls)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateModelFromDto(@MappingTarget ProductTagModel target, ProductTagUpdateDto dto);

    //MODEL --> RESPONSE
    ProductTagResponseDto toResponse(ProductTagModel model);
    List<ProductTagResponseDto> toResponseList(List<ProductTagModel> models);

    default Page<ProductTagResponseDto> toResponsePage(Page<ProductTagModel> page){
        return page.map(this::toResponse);
    }
}
