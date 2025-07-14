package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductResponseMapper {

    public static List<ProductResponseDto> toResponseDTOList(List<ProductModel> content) {
        if (content == null || content.isEmpty()) {
            return List.of();
        }

        return content.stream()
                .map(model -> ProductResponseDto.builder()
                        .id(model.getId())
                        .code(model.getCode())
                        .name(model.getName())
                        .description(model.getDescription())
                        .categoryId(model.getCategoryId())
                        .categoryName(null)  // ← lo puedes enriquecer luego
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
                        .build()
                )
                .toList();
    }

    public ProductResponseDto toDto(ProductModel model) {
        if (model == null) {
            return null;
        }

        return ProductResponseDto.builder()
                .id(model.getId())
                .code(model.getCode())
                .name(model.getName())  // ✅ AGREGAR
                .description(model.getDescription())
                .categoryId(model.getCategoryId())  // ✅ CAMBIAR: productTypeCode → categoryId
                .categoryName(null)  // ✅ AGREGAR: Por ahora null, después podemos mejorarlo
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

    //TODO: NO SE USA
    public static ProductResponseDto toDtoWithCategoryName(ProductModel model, String categoryName) {
        if (model == null) {
            return null;
        }

        return ProductResponseDto.builder()
                .id(model.getId())
                .code(model.getCode())
                .name(model.getName())
                .description(model.getDescription())
                .categoryId(model.getCategoryId())
                .categoryName(categoryName)  // Nombre obtenido externamente
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

    public static List<ProductResponseDto> toResponseDtoList(List<ProductModel> models) {
        if (models == null) return null;
        return models.stream()
                .map(new ProductResponseMapper()::toDto) // o si haces static el toDto, lo puedes cambiar
                .collect(Collectors.toList());
    }
}