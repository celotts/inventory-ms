// Archivo: src/main/java/com/celotts/productservice/infrastructure/adapter/input/rest/mapper/productbrand/ProductBrandDtoMapper.java
package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productbrand;

import com.celotts.productservice.domain.model.product.ProductBrandModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductBrandDtoMapper {

    /**
     * Convierte ProductBrandCreateDto a ProductBrandModel
     * Auditoría y defaults (enabled=true si null) se manejan en el use case/JPA.
     */
    public ProductBrandModel toModel(ProductBrandCreateDto createDto) {
        if (createDto == null) return null;

        return ProductBrandModel.builder()
                .name(createDto.getName() != null ? createDto.getName().trim() : null)
                .description(createDto.getDescription())
                .enabled(createDto.getEnabled()) // null -> default en use case
                .build();
    }

    /**
     * Convierte ProductBrandModel a ProductBrandResponseDto
     */
    public ProductBrandResponseDto toResponseDto(ProductBrandModel model) {
        if (model == null) return null;

        return ProductBrandResponseDto.builder()
                .id(model.getId())
                .name(model.getName())
                .description(model.getDescription())
                .enabled(model.getEnabled())
                .createdBy(model.getCreatedBy())
                .updatedBy(model.getUpdatedBy())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    /**
     * Convierte lista de ProductBrandModel a lista de ProductBrandResponseDto
     */
    public List<ProductBrandResponseDto> toResponseDtoList(List<ProductBrandModel> models) {
        if (models == null) return null;
        return models.stream().map(this::toResponseDto).collect(Collectors.toList());
    }


}