package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.shared.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "product")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductEntity extends AuditableEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "product_type", length = 50, nullable = false)
    private String productType;   // referencia al code de product_type

    @Column(name = "unit_id", nullable = false)
    private UUID unitId;

    @Column(name = "brand_id")
    private UUID brandId;

    @Column(name = "minimum_stock", precision = 10, scale = 2)
    private BigDecimal minimumStock;

    @Column(name = "current_stock", precision = 10, scale = 2)
    private BigDecimal currentStock;

    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;
}