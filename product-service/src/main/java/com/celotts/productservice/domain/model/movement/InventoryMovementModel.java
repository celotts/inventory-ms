package com.celotts.productservice.domain.model.movement;

import com.celotts.productservice.domain.model.common.AuditLite;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record InventoryMovementModel(
        UUID id,
        UUID productId,
        UUID lotId,
        MovementType type,
        MovementPurpose purpose,
        BigDecimal quantity,
        String reference,
        String reason,
        Instant occurredAt,
        AuditLite audit
) {}