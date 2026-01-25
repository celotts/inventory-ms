package com.celotts.purchaseservice.infrastructure.adapter.output.postgres.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "purchase_orders") // Usa plural para evitar errores con SQL "ORDER BY"
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class PurchaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "supplier_id", nullable = false)
    private UUID supplierId;

    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(length = 3)
    private String currency;

    @Column(precision = 14, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "tax_total", precision = 14, scale = 2)
    private BigDecimal taxTotal;

    @Column(name = "discount_total", precision = 14, scale = 2)
    private BigDecimal discountTotal;

    @Column(name = "grand_total", precision = 14, scale = 2)
    private BigDecimal grandTotal;

    @Column(name = "expected_at")
    private LocalDate expectedAt;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    @Column(length = 255)
    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "DRAFT";
    }
}