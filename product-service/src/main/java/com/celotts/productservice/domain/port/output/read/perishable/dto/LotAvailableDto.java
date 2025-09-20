package com.celotts.productservice.domain.port.output.read.perishable.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record LotAvailableDto(
        UUID lotId,
        UUID productId,
        String lotCode,
        BigDecimal quantity,
        LocalDate expirationDate,
        String stage,
        Instant receivedAt
) {}