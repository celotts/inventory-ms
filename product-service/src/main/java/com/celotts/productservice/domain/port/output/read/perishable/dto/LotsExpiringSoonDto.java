package com.celotts.productservice.domain.port.output.read.perishable.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record LotsExpiringSoonDto(
        UUID productId,
        String code,
        String name,
        UUID lotId,
        String lotCode,
        LocalDate expirationDate,
        BigDecimal quantity
) {}