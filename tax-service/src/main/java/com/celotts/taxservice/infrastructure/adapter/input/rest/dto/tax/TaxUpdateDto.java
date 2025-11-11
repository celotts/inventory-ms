package com.celotts.taxservice.infrastructure.adapter.input.rest.dto.tax;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxUpdateDto {

    @Size(max = 30, message = "{tax.code.max-length}")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "{tax.code.pattern}")
    private String code;

    @Size(min = 2, max = 120, message = "{tax.name.size}")
    private String name;

    @DecimalMin(value = "0.00", inclusive = true, message = "{tax.rate.min}")
    @DecimalMax(value = "100.00", inclusive = true, message = "{tax.rate.max}")
    @Digits(integer = 3, fraction = 2, message = "{tax.rate.digits}")
    private BigDecimal rate;

    private LocalDate validFrom;

    private LocalDate validTo;

    private Boolean isActive;
}