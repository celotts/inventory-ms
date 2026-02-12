package com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.tax;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a tax configuration from tax-service")
public class TaxDto {
    private UUID id;
    private String name;
    private String code;
    private String description;
    private BigDecimal rate; // Porcentaje del impuesto (ej. 16.00 para 16%)
    private Boolean isActive;
}
