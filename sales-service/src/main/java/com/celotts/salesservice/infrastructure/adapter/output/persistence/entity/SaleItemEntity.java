package com.celotts.salesservice.infrastructure.adapter.output.persistence.entity;

import com.celotts.salesservice.domain.model.PreparationArea;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "sale_items")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class SaleItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private SaleEntity sale;

    private UUID productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    @Enumerated(EnumType.STRING)
    private PreparationArea area;
}