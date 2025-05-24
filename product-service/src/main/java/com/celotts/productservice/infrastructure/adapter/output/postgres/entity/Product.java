package com.celotts.productservice.infrastructure.adapter.output.postgres.entity;

import com.celotts.productservice.infrastructure.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    // ✅ Aquí está el campo clave:
    // productTypeCode en Java → mapeado a product_type en la base de datos
    @Column(name = "product_type", length = 50, nullable = false)
    private String productTypeCode;

    @Column(name = "unit_code", length = 50)
    private String unitCode;

    @Column(name = "brand_id")
    private UUID brandId;

    @Column(name = "minimum_stock", nullable = false)
    private Integer minimumStock;

    @Column(name = "current_stock", nullable = false)
    private Integer currentStock;

    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private Boolean enabled = true;
}