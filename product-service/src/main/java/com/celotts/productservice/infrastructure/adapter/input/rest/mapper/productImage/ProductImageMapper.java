package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productImage;

import com.celotts.productservice.domain.model.product.ProductImageModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productImage.ProductImageCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productImage.ProductImageUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productImage.ProductImageResponseDto;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    // CREATE DTO -> MODEL
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    ProductImageModel toModel(ProductImageCreateDto dto);

    // UPDATE parcial (ignora nulls)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateModelFromDto(@MappingTarget ProductImageModel target, ProductImageUpdateDto dto);

    // MODEL -> RESPONSE
    ProductImageResponseDto toResponse(ProductImageModel model);
    List<ProductImageResponseDto> toResponseList(List<ProductImageModel> models);

    // PAGE helper
    default Page<ProductImageResponseDto> toResponsePage(Page<ProductImageModel> page) {
        return page.map(this::toResponse);
    }

}
