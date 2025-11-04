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
public class TaxCreateDto {

    @NotBlank(message = "{tax.code.required}")
    @Pattern(regexp = "^[A-Z0-9\\-_]{3,40}$", message = "{tax.code.pattern}")
    private String code;

    @NotBlank(message = "{tax.name.required}")
    @Size(min = 2, max = 120, message = "{tax.name.size}")
    private String name;

    @NotNull(message = "{tax.rate.required}")
    @DecimalMin(value = "0.00", inclusive = true, message = "{tax.rate.min}")
    @DecimalMax(value = "100.00", inclusive = true, message = "{tax.rate.max}")
    @Digits(integer = 3, fraction = 2, message = "{tax.rate.digits}")
    private BigDecimal rate;

    @PastOrPresent(message = "{tax.validfrom.pastOrPresent}")
    @Builder.Default
    private LocalDate validFrom = LocalDate.now();

    @FutureOrPresent(message = "{tax.validto.futureOrPresent}")
    private LocalDate validTo;

    @Builder.Default
    private Boolean isActive = true;
}