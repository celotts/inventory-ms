package com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_brand")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductBrandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String description;
    private Boolean enabled;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public ProductBrandEntity(UUID id, String name, String description, LocalDateTime createdAt) {
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
