package com.celotts.productservice.infrastructure.adapter.output.postgres.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true,  length = 50)
    private String code;

    private String description;

    @ManyToOne
    @JoinColumn(name="product_type", referencedColumnName = "code")
    private ProductType productType;

    @ManyToOne
    @JoinColumn(name= "unit_code", referencedColumnName = "code")
    private ProductUnit productUnit;

    @ManyToOne
    @JoinColumn(name= "brand_id")
    private ProductBrand productBrand;

    @Column(name="minimum_stock", precision = 10, scale = 2)
    private BigDecimal minimumStock;

    @Column(name="current_stock", precision = 10, scale = 2)
    private BigDecimal currentStock;

    @Column(name="unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrince;

    private Boolean enabled= true;

    @Column(name="created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    private String createdBy; // Aquí asumimos que es una cadena de usuario o IP si luego lo deseas
    private String updatedBy;
}





