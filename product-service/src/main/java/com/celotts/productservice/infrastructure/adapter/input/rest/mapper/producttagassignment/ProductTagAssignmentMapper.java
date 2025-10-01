package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.producttagassignment;

import com.celotts.productservice.domain.model.product.ProductTagAssignmentModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttagassignment.ProductTagAssignmentCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttagassignment.ProductTagAssignmentUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttagassignment.ProductTagAssignmentResponseDto;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel =  "spring")
public interface ProductTagAssignmentMapper {

    // CREATE DTO -> MODEL
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    ProductTagAssignmentModel toModel(ProductTagAssignmentCreateDto dto);

    // UPDATE parcial (ignora nulls)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateModelFromDto(@MappingTarget ProductTagAssignmentModel target,
                            ProductTagAssignmentUpdateDto dto);

    ProductTagAssignmentResponseDto toResponse(ProductTagAssignmentModel model);
    List<ProductTagAssignmentResponseDto> toResponsableList(List<ProductTagAssignmentModel> models);

    default Page<ProductTagAssignmentResponseDto> toResponsePage(Page<ProductTagAssignmentModel> page) {
        return page.map(this::toResponse);
    }
}

