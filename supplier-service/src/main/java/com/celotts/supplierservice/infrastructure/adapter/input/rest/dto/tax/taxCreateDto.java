package com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.tax;

import com.celotts.supplierservice.infrastructure.common.validation.ValidationGroups;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.netflix.spectator.api.Statistic.max;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class taxCreateDto {
    /*
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  code VARCHAR(30) NOT NULL,              -- p.ej. 'IVA16'
  name VARCHAR(120) NOT NULL,             -- p.ej. 'IVA 16% MX'
  rate DECIMAL(5,2) NOT NULL CHECK (rate >= 0 AND rate <= 100),
  jurisdiction VARCHAR(60),               -- p.ej. 'MX'
  tax_type VARCHAR(30),                   -- p.ej. 'VAT','IEPS'
  valid_from DATE NOT NULL DEFAULT CURRENT_DATE,
  valid_to DATE,                          -- NULL = vigente
  enabled BOOLEAN NOT NULL DEFAULT TRUE,

  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
     */
    @NotBlank(message = "supplier.code.pattern)", groups = ValidationGroups.Create.class)
    @Pattern(
            regexp = "^[A-Z0-9\\-_]{3,40}$",
            message = "{tax.code.pattern}",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class}
    )
    private String Code;

    @NotBlank(message = "{tax.name.required}", groups = ValidationGroups.Create.class)
    @Size(
            min = 2, max = 150,
            message = "{supplier.name.size}",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class}
    )
    private String Name;


    @NotNull(message = "{tax.rate.required}")
    @DecimalMin(value = "0.00", inclusive = true, message = "{tax.rate.min}")
    @DecimalMax(value="100.00", inclusive = true, message = "{tax.rate.max}")
    @Digits(integer =  3, fraction = 2, message = "{tax.rate.digits}")
    private BigDecimal rate;

    @Size(max = 60, message = "{tax.jurisdiction.size}")
    private String jurisdiction;

    @Size(max = 30, message = "{tax.type.size}")
    private String taxType;

    @Builder.Default
    private LocalDate validFrom = LocalDate.now();

    @FutureOrPresent(message = "{tax.validfrom.pastOrPresent}")
    private LocalDate validTo;

    @Builder.Default
    private Boolean enabled = true;
}
