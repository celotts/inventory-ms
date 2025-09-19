package com.celotts.productservice.domain.model.views;

public record LotAvailableView (
    UUID id, UUID productId, String lotCode, BigDecimal quantity, BigDecimal unitCost, Instant receivedAt, LocalDate mfgDate, LocalDate expirationDate, LotStage stage, Boolean enabled
) {}
