package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productImage;

import com.celotts.productservice.domain.model.product.ProductImageModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productImage.ProductImageCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productImage.ProductImageUpdateDto;
import com.celotts.productservice.infrastructure.common.util.MapperUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductImageModelRequestMapper {

    public void updateModelFromDto(ProductImageModel existingModel, @Valid ProductImageUpdateDto dto) {
        if (existingModel == null || dto == null) return;

        if (nonBlank(dto.getUrl())) existingModel.setUrl(dto.getUrl().trim());
        if (dto.getEnabled() != null) existingModel.setEnabled(dto.getEnabled());
        if (dto.getUploadedAt() != null) existingModel.setUploadedAt(dto.getUploadedAt());
        if (nonBlank(dto.getUpdatedBy())) existingModel.setUpdatedBy(dto.getUpdatedBy().trim());

        existingModel.setUpdateAt(LocalDateTime.now());
    }

    /**
     * Convierte un DTO de creación en un modelo de dominio listo para persistir.
     */
    /** Crear (insert) */
    public ProductImageModel toModel(ProductImageCreateDto dto) {
        if (dto == null) return null;

        return ProductImageModel.builder()
                .productId(dto.getProductId())
                .url(nonBlank(dto.getUrl()) ? dto.getUrl().trim() : null)
                .enabled(dto.getEnabled() != null ? dto.getEnabled() : Boolean.TRUE)
                .active(Boolean.TRUE) // por defecto activo al crear
                .uploadedAt(dto.getUploadedAt() != null ? dto.getUploadedAt() : LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .createdBy(nonBlank(dto.getCreatedBy()) ? dto.getCreatedBy().trim() : "system")
                .build();
    }

    /**
     * Convierte un DTO de actualización en un modelo de dominio parcial.
     * Útil en algunos casos si quieres crear una copia con cambios.
     */
    public ProductImageModel toModel(ProductImageUpdateDto dto) {
        if (dto == null) return null;

        return ProductImageModel.builder()
                .url(nonBlank(dto.getUrl()) ? dto.getUrl().trim() : null)
                .enabled(dto.getEnabled())
                .uploadedAt(dto.getUploadedAt())
                .updatedBy(nonBlank(dto.getUpdatedBy()) ? dto.getUpdatedBy().trim() : "system")
                .updateAt(LocalDateTime.now())
                .build();
    }


    private static boolean nonBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }
}


