package com.celotts.productservice.domain.model.product;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
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

    // Reglas de negocio
    public boolean isActive() { return Boolean.TRUE.equals(enabled); }
    public void activate()     { this.enabled = true; }
    public void deactivate()   { this.enabled = false; }


}