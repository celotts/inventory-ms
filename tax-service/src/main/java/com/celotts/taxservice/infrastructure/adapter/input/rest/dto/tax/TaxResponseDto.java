package com.celotts.taxservice.infrastructure.adapter.input.rest.dto.tax;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO usado para devolver información al cliente.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxResponseDto {

    private UUID id;
    private String code;
    private String name;
    private BigDecimal rate;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Boolean isActive;
    private OffsetDateTime updatedAt;
}