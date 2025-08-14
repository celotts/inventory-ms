package com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_tag")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // <-- agrega (import lombok.Builder)
public class ProductTagEntity {

    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    private String description;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    // ✅ Constructor que esperan los tests
    public ProductTagEntity(UUID id, String name, String description, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.enabled = true;        // por defecto
        this.createdAt = createdAt; // viene del test (FIXED_TIME)
        this.updatedAt = null;
        this.createdBy = null;
        this.updatedBy = null;
    }
}