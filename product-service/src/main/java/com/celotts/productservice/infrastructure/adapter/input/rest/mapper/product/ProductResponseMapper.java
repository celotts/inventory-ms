package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductResponseMapper {

    public ProductResponseDTO toDto(ProductModel model) {
        if (model == null) {
            return null;
        }

        return ProductResponseDTO.builder()
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
    public static ProductResponseDTO toDtoWithCategoryName(ProductModel model, String categoryName) {
        if (model == null) {
            return null;
        }

        return ProductResponseDTO.builder()
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

    public static List<ProductResponseDTO> toResponseDtoList(List<ProductModel> models) {
        if (models == null) return null;
        return models.stream()
                .map(new ProductResponseMapper()::toDto) // o si haces static el toDto, lo puedes cambiar
                .collect(Collectors.toList());
    }
}