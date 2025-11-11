package com.celotts.taxservice.infrastructure.adapter.input.rest.dto.tax;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Generic request DTO for Tax operations.
 * Can be used for both create and update operations.
 * All validation messages are internationalized.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxRequestDto {

    @NotBlank(message = "{tax.code.required}")
    @Size(max = 30, message = "{tax.code.max-length}")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "{tax.code.pattern}")
    private String code;

    @NotBlank(message = "{tax.name.required}")
    @Size(min = 2, max = 120, message = "{tax.name.size}")
    private String name;

    @NotNull(message = "{tax.rate.required}")
    @DecimalMin(value = "0.00", inclusive = true, message = "{tax.rate.min}")
    @DecimalMax(value = "100.00", inclusive = true, message = "{tax.rate.max}")
    @Digits(integer = 3, fraction = 2, message = "{tax.rate.digits}")
    private BigDecimal rate;

    @NotNull(message = "{tax.validfrom.required}")
    private LocalDate validFrom;

    private LocalDate validTo;

    @Builder.Default
    private Boolean isActive = true;
}