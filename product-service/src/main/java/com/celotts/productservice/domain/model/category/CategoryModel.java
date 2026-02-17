package com.celotts.productservice.domain.model.category;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CategoryModel {
    private UUID id;
    private String name;
    private String description;
    private Boolean active; // Renombrado de 'enabled' a 'active' para coincidir con los tests
    private boolean deleted; // Agregado porque los tests lo requieren
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Método requerido por CategoryModelTest
    public void update(String name, String description, boolean active, String updatedBy) {
        this.name = name;
        this.description = description;
        this.active = active;
        this.updatedBy = updatedBy;
        this.updatedAt = LocalDateTime.now();
    }
}
