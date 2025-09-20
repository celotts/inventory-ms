package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.lot;

import com.celotts.productservice.domain.model.lot.LotStage;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "lot")
@Getter @Setter
public class LotEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "product_id", nullable = false, columnDefinition = "uuid")
    private UUID productId;

    @Column(name = "lot_code", nullable = false, length = 50)
    private String lotCode;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unit_cost", precision = 12, scale = 2)
    private BigDecimal unitCost;

    @Column(name = "received_at")
    private Instant receivedAt;

    @Column(name = "mfg_date")
    private LocalDate mfgDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "stage", columnDefinition = "lot_stage", nullable = false)
    private LotStage stage;

    @Column(name = "supplier_id", columnDefinition = "uuid")
    private UUID supplierId;

    @Column(name = "notes", length = 255)
    private String notes;

    private Boolean enabled;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by", length = 255)
    private String  createdBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by", length = 255)
    private String  updatedBy;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "deleted_by", length = 255)
    private String  deletedBy;

    @Column(name = "deleted_reason", length = 255)
    private String deletedReason;
}
