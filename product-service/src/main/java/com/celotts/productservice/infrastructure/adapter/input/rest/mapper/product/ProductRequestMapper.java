package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product;

import com.celotts.productservice.domain.model.product.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductUpdateDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import java.math.RoundingMode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class ProductRequestMapper {

    public void updateModelFromDto(ProductModel existingModel, @Valid ProductUpdateDto dto) {
        if (dto == null || existingModel == null) return;

        // code y createdBy suelen ser inmutables -> NO tocarlos en update
        if (nonBlank(dto.getName())) existingModel.setName(dto.getName().trim());
        if (nonBlank(dto.getDescription())) existingModel.setDescription(dto.getDescription().trim());
        if (nonBlank(dto.getUnitCode())) existingModel.setUnitCode(dto.getUnitCode().trim().toUpperCase());
        if (dto.getBrandId() != null) existingModel.setBrandId(dto.getBrandId());
        if (dto.getCategoryId() != null) existingModel.setCategoryId(dto.getCategoryId());
        if (dto.getMinimumStock() != null) existingModel.setMinimumStock(dto.getMinimumStock());
        if (dto.getCurrentStock() != null) existingModel.setCurrentStock(dto.getCurrentStock());
        if (dto.getUnitPrice() != null) existingModel.setUnitPrice(scale(dto.getUnitPrice()));
        if (dto.getEnabled() != null) existingModel.setEnabled(dto.getEnabled());
        if (nonBlank(dto.getUpdatedBy())) existingModel.setUpdatedBy(dto.getUpdatedBy().trim());

        existingModel.setUpdatedAt(LocalDateTime.now());
    }

    public ProductModel toModel(ProductCreateDto dto) {
        if (dto == null) return null;

        return ProductModel.builder()
                .code(nonBlank(dto.getCode()) ? dto.getCode().trim().toUpperCase() : null)
                .name(nonBlank(dto.getName()) ? dto.getName().trim() : null)
                .description(nonBlank(dto.getDescription()) ? dto.getDescription().trim() : null)
                .categoryId(dto.getCategoryId())
                .unitCode(nonBlank(dto.getUnitCode()) ? dto.getUnitCode().trim().toUpperCase() : null)
                .brandId(dto.getBrandId())
                .minimumStock(dto.getMinimumStock())
                .currentStock(dto.getCurrentStock())
                .unitPrice(scale(dto.getUnitPrice()))
                .enabled(Boolean.TRUE.equals(dto.getEnabled()))
                .createdBy(nonBlank(dto.getCreatedBy()) ? dto.getCreatedBy().trim() : null)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public ProductModel toModel(ProductUpdateDto dto) {
        if (dto == null) return null;

        return ProductModel.builder()
                // code/createdBy fuera de update; si decides permitirlos, añade aquí
                .name(nonBlank(dto.getName()) ? dto.getName().trim() : null)
                .description(nonBlank(dto.getDescription()) ? dto.getDescription().trim() : null)
                .categoryId(dto.getCategoryId())
                .unitCode(nonBlank(dto.getUnitCode()) ? dto.getUnitCode().trim().toUpperCase() : null)
                .brandId(dto.getBrandId())
                .minimumStock(dto.getMinimumStock())
                .currentStock(dto.getCurrentStock())
                .unitPrice(scale(dto.getUnitPrice()))
                .enabled(dto.getEnabled())
                .updatedBy(nonBlank(dto.getUpdatedBy()) ? dto.getUpdatedBy().trim() : null)
                .build();
    }


    private static boolean nonBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private static BigDecimal scale(BigDecimal v) {
        return v == null ? null : v.setScale(2, RoundingMode.HALF_UP);
    }
}