package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_unit")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUnitEntity {

    @Id
    @Column(name = "code", length = 30)
    private String code;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "symbol", length = 10)
    private String symbol;
}