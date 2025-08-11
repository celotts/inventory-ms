package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductResponseDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
@Component
public class ProductResponseMapper {

    public ProductResponseDto toDto(ProductModel model) {
        if (model == null) return null;

        return ProductResponseDto.builder()
                .id(model.getId())
                .code(model.getCode())
                .name(model.getName())
                .description(model.getDescription())
                .categoryId(model.getCategoryId())
                .categoryName(null) // puedes enriquecerlo luego
                .unitCode(model.getUnitCode())
                .brandId(model.getBrandId())
                .minimumStock(model.getMinimumStock())
                .currentStock(model.getCurrentStock())
                .unitPrice(model.getUnitPrice())
                .enabled(model.getEnabled())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .createdBy(model.getCreatedBy())
                .updatedBy(model.getUpdatedBy())
                .lowStock(model.lowStock())
                .build();
    }

    public ProductResponseDto toResponseDto(ProductModel model) {
        return toDto(model);
    }

    public ProductResponseDto toDtoWithCategoryName(ProductModel model, String categoryName) {
        if (model == null) return null;

        ProductResponseDto dto = toDto(model);
        return ProductResponseDto.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .categoryId(dto.getCategoryId())
                .categoryName(categoryName) // inyectado
                .unitCode(dto.getUnitCode())
                .brandId(dto.getBrandId())
                .minimumStock(dto.getMinimumStock())
                .currentStock(dto.getCurrentStock())
                .unitPrice(dto.getUnitPrice())
                .enabled(dto.getEnabled())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getUpdatedBy())
                .lowStock(dto.getLowStock())
                .build();
    }

    public List<ProductResponseDto> toDtoList(List<ProductModel> models) {
        if (models == null || models.isEmpty()) {
            return Collections.emptyList();
        }

        return models.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDto> toResponseDtoList(List<ProductModel> models) {
        return toDtoList(models);
    }


    public Page<ProductResponseDto> toDtoPage(Page<ProductModel> page) {
        if (page == null || page.isEmpty()) {
            return Page.empty();
        }

        return new PageImpl<>(
                page.getContent().stream()
                        .map(this::toDto)
                        .collect(Collectors.toList()),
                page.getPageable(),
                page.getTotalElements()
        );
    }
}