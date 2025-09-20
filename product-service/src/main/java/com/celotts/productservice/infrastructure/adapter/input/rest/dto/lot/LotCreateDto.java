package com.celotts.productservice.infrastructure.adapter.input.rest.dto.lot;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record LotCreateDto(
        @NotNull UUID productId,
        @NotBlank String lotCode,
        @Positive BigDecimal quantity,
        @PositiveOrZero BigDecimal unitCost,
        LocalDate mfgDate,
        LocalDate expirationDate,
        String notes
) {}
