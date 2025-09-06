package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productCategory;

import com.celotts.productservice.domain.model.product.ProductCategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {

    ProductCategoryModel toModel(ProductCategoryCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModelFromDto(@MappingTarget ProductCategoryModel target, ProductCategoryUpdateDto dto);

    ProductCategoryResponseDto toResponse(ProductCategoryModel model);
    List<ProductCategoryResponseDto> toResponseList(List<ProductCategoryModel> models);
}