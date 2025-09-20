package com.celotts.productservice.domain.model.views;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import com.celotts.productservice.domain.model.lot.LotStage;

public record LotExpiredView(
        UUID id, UUID productId, String lotCode, BigDecimal quantity, BigDecimal unitCost,
        Instant receivedAt, LocalDate mfgDate, LocalDate expirationDate, LotStage stage,
        Boolean enabled
) {}