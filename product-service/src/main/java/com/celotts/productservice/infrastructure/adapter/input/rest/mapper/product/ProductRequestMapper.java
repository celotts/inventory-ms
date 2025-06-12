package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductUpdateDTO;

import java.time.LocalDateTime;

public class ProductRequestMapper {

    /**
     * Actualiza un ProductModel existente con datos del ProductUpdateDTO
     * Solo actualiza campos que no son null
     */
    public static void updateModelFromDto(ProductModel existingModel, ProductUpdateDTO dto) {
        if (dto == null) return;

        if (dto.getCode() != null) {
            existingModel.setCode(dto.getCode());
        }
        if (dto.getName() != null) {
            existingModel.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            existingModel.setDescription(dto.getDescription());
        }
        if (dto.getUnitCode() != null) {
            existingModel.setUnitCode(dto.getUnitCode());
        }
        if (dto.getBrandId() != null) {
            existingModel.setBrandId(dto.getBrandId());
        }
        if (dto.getCategoryId() != null) {
            existingModel.setCategoryId(dto.getCategoryId());
        }
        if (dto.getMinimumStock() != null) {
            existingModel.setMinimumStock(dto.getMinimumStock());
        }
        if (dto.getCurrentStock() != null) {
            existingModel.setCurrentStock(dto.getCurrentStock());
        }
        if (dto.getUnitPrice() != null) {
            existingModel.setUnitPrice(dto.getUnitPrice());
        }
        if (dto.getEnabled() != null) {
            existingModel.setEnabled(dto.getEnabled());
        }
        if (dto.getUpdatedBy() != null) {
            existingModel.setUpdatedBy(dto.getUpdatedBy());
        }

        // Siempre actualizar fecha de modificación
        existingModel.setUpdatedAt(LocalDateTime.now());
    }
}