package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productpricehistory;

import com.celotts.productservice.domain.model.product.ProductPriceHistoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productpricehistory.ProductPriceHistoryCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productpricehistory.ProductPriceHistoryResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productpricehistory.ProductPriceHistoryUpdateDto;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ProductPriceHistoryMapper {

    // CREATE DTO --> MODEL
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    ProductPriceHistoryModel toModel(ProductPriceHistoryCreateDto dto);

    // CREATE DTO --> MODEL (con productId proveniente del path)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", source = "productId") // 👈 setea el productId del path
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    ProductPriceHistoryModel toModel(ProductPriceHistoryCreateDto dto, UUID productId);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore= true )
    void updateModelFromDto(@MappingTarget ProductPriceHistoryModel target, ProductPriceHistoryUpdateDto dto);


    ProductPriceHistoryResponseDto toResponse(ProductPriceHistoryModel model);
    List<ProductPriceHistoryResponseDto> toResponseList(List<ProductPriceHistoryModel> models);

    default Page<ProductPriceHistoryResponseDto> toResponsePage(Page<ProductPriceHistoryModel> page) {
        return page.map(this::toResponse);
    }

}
