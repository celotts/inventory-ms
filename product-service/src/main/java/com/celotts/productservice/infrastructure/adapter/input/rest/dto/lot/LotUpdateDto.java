package com.celotts.productservice.infrastructure.adapter.input.rest.dto.lot;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LotUpdateDto(
        @NotBlank String lotCode,
        @Positive BigDecimal quantity,
        @PositiveOrZero BigDecimal unitCost,
        LocalDate mfgDate,
        LocalDate expirationDate,
        String notes

) { }
