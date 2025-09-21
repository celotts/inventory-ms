package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productbrand;

import com.celotts.productservice.domain.model.product.ProductBrandModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productbrand.ProductBrandCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productbrand.ProductBrandUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productbrand.ProductBrandResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE // evita errores por campos no mapeados (p.ej. deleted*)
)
public interface ProductBrandMapper {

    // CREATE: no seteamos id/created*/updated*/deleted*; enabled true por defecto (si aplica)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deletedReason", ignore = true)
    ProductBrandModel toModel(ProductBrandCreateDto dto);

    // UPDATE (PATCH/PUT): aplica solo campos no nulos del DTO; nunca toques id/created*/deleted*
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "deletedReason", ignore = true)
    void updateModelFromDto(@MappingTarget ProductBrandModel target, ProductBrandUpdateDto dto);

    // Response
    ProductBrandResponseDto toResponse(ProductBrandModel model);
    List<ProductBrandResponseDto> toResponseList(List<ProductBrandModel> models);
}