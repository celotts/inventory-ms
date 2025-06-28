package com.celotts.productservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    // Métodos de negocio si los necesitas
    public boolean isActive() {
        return enabled != null && enabled;
    }

    /**
     * Marca esta entidad como habilitada.
     * Puede usarse en flujos donde se reactive una marca previamente deshabilitada.
     */
    //TODO: NO SE USA
    public void activate() {
        this.enabled = true;
    }
    /**
     * Marca esta entidad como habilitada.
     * Puede usarse en flujos donde se reactive una marca previamente habilitada.
     */
    //TODO: NO SE USA
    public void deactivate() {
        this.enabled = false;
    }
}