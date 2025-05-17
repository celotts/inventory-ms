package com.celotts.productservice.infrastructure.adapter.output.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_unit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUnit {

    @Id
    @Column(length = 30)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 10)
    private String symbol;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}