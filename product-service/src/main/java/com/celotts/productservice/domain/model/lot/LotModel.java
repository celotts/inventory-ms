package com.celotts.productservice.domain.model.lot;

import com.celotts.productservice.domain.model.common.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LotModel {
    private UUID id;
    private UUID productId;
    private String lotCode;
    private BigDecimal quantity;
    private BigDecimal unitCost;
    private LocalDateTime receivedAt; // Cambiado a LocalDateTime para consistencia
    private LocalDate mfgDate;
    private LocalDate expirationDate;
    private LotStage stage;
    private UUID supplierId;
    private String notes;
    private Boolean enabled;
    
    // Campos de auditoría aplanados o embebidos
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;
    private String deletedReason;
}
