package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.producttype;

import com.celotts.productservice.domain.model.product.ProductTypeModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttype.ProductTypeCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttype.ProductTypeResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttype.ProductTypeUpdateDto;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

;

@Mapper(componentModel = "spring")
public interface ProductTypeMapper {

    // CREATE DTO -> MODEL
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    ProductTypeModel toModel(ProductTypeCreateDto dto);

    // UPDATE parcial (ignore nulls)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateModelFromDto(@MappingTarget ProductTypeModel target, ProductTypeUpdateDto dto);

    ProductTypeResponseDto toResponse(ProductTypeModel model);
    List<ProductTypeResponseDto> toResponseList(List<ProductTypeModel> models);

    default Page<ProductTypeResponseDto> toResponsePage(Page<ProductTypeModel> page) {return page.map(this::toResponse);}
}
