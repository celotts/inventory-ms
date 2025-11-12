package com.celotts.productservice.domain.model.product;

import lombok.*;

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

    // estado
    private Boolean enabled;   // legacy (lo siguen usando algunos mappers)
    private Boolean active;    // el test usa isActive()

    // auditoría
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    // “soft delete”
    private LocalDateTime deletedAt;
    private String deletedBy;
    private String deletedReason;

    // ---- constructores que usa el test ----
    public ProductBrandModel(UUID id,
                             String name,
                             String description,
                             boolean active,
                             String createdBy,
                             String updatedBy,
                             LocalDateTime createdAt,
                             LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.enabled = active;          // sincroniza con enabled
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ProductBrandModel(UUID id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.enabled = active;          // sincroniza con enabled
    }

    // ---- comportamiento de dominio ----
    public boolean isActive() {
        return Boolean.TRUE.equals(this.active);
    }

    public void activate() {
        this.active = true;
        this.enabled = true;            // mantén ambas en sync para mapeos viejos
    }

    public void deactivate() {
        this.active = false;
        this.enabled = false;
    }
}