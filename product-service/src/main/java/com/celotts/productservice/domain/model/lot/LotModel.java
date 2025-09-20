package com.celotts.productservice.domain.model.lot;

import com.celotts.productservice.domain.model.common.AuditModel;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record LotModel(
        UUID id,
        UUID productId,
        String lotCode,
        BigDecimal quantity,
        BigDecimal unitCost,
        Instant receivedAt,
        LocalDate mfgDate,
        LocalDate expirationDate,
        LotStage stage,
        UUID supplierId,
        String notes,
        Boolean enabled,
        AuditModel audit
) {}