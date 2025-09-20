package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.movement;

import com.celotts.productservice.domain.model.movement.MovementPurpose;
import com.celotts.productservice.domain.model.movement.MovementType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inventory_movement")
@Getter @Setter
public class InventoryMovementEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "product_id", nullable = false, columnDefinition = "uuid")
    private UUID productId;

    @Column(name = "lot_id", columnDefinition = "uuid")
    private UUID lotId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementPurpose purpose;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quantity;

    private String reference;
    private String reason;

    @Column(name="occurred_at")
    private Instant occurredAt;

    @Column(name="created_at")
    private Instant createdAt;

    @Column(name="created_by", length = 255)
    private String createdBy;

    @Column(name="deleted_at") private Instant deletedAt;

    @Column(name="deleted_by", length = 255)
    private String deletedBy;
}
