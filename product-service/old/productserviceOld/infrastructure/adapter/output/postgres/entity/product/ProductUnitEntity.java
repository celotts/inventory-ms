package com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_unit")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUnitEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String code;          //  ← NUEVO

    private String name;
    private String description;

    @Column(nullable = false)
    private String symbol; // ✅ AGREGADO

    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}