package com.celotts.purchaseservice.infrastructure.adapter.output.postgres.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "purchase_orders")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class PurchaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "supplier_id", nullable = false, updatable = false)
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

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by", length = 120)
    private String deletedBy;

    @Column(name = "deleted_reason", length = 255)
    private String deletedReason;

    // --- CAMPOS DE AUDITORÍA ---
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 120, updatable = false)
    private String createdBy;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.createdBy == null) {
            this.createdBy = "SYSTEM";
        }

        if (this.status == null) this.status = "DRAFT";
        if (this.currency == null) this.currency = "MXN";
    }

    @Column(name = "updated_by", length = 120)
    private String updatedBy;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}