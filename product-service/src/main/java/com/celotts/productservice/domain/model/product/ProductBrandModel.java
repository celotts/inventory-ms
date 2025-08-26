package com.celotts.productservice.domain.model.product;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@With
@AllArgsConstructor
@NoArgsConstructor
public class ProductBrandModel {
    private UUID id;
    private String name;
    private String description;
    private Boolean enabled;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductBrandModel(UUID brandId, String coolBrand, boolean b) {
    }

    // Métodos de negocio si los necesitas
    public boolean isActive() {
        return enabled != null && enabled;
    }


    public void activate() {
        this.enabled = true;
    }

    public void deactivate() {
        this.enabled = false;
    }


}