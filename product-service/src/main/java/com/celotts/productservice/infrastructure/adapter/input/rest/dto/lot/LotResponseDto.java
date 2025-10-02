package com.celotts.productservice.infrastructure.adapter.input.rest.dto.lot;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record LotResponseDto(
    UUID id,
    UUID productId,
    String lotCode,
    BigDecimal quantity,
    BigDecimal unitCost,
    LocalDate mfgDate,
    LocalDate expirationDate,
    String notes,
    Boolean expired,
    Boolean disposed
) {}
