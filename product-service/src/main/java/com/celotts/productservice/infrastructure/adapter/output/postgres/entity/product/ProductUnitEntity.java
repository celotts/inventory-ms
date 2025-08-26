package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product;

import com.celotts.productservice.infrastructure.common.jpa.BaseEntity; // <-- este paquete
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "product_unit")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductUnitEntity extends BaseEntity {

    @Id
    @GeneratedValue
    @org.hibernate.annotations.UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;                      // PK real


    @Column(name = "code", length = 30, nullable = false, unique = true)
    private String code;                  // clave de negocio única

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "symbol", length = 10, nullable = false)
    private String symbol;

    @Column(name = "description", length = 100, nullable = false)
    private String description;

    @Column(name = "enabled")
    private Boolean enabled;

}