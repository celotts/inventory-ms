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
    @Size(max = 40, message = "{tax.code.max-length}")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "{tax.code.pattern}")
    private String code;

    @NotBlank(message = "{tax.name.required}")
    @Size(min = 2, max = 120, message = "{tax.name.size}")
    private String name;

    @Size(min = 2, max = 120, message = "{tax.description.size}")
    private String description;

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

    @NotBlank(message = "{tax.createdBy.required}")
    private String createdBy;

    @AssertTrue(message = "{tax.date.range.invalid}")
    public boolean isDateRangeValid() {
        if (validFrom == null || validTo == null) {
            return true; // Dejamos que @NotNull maneje la obligatoriedad de validFrom
        }
        return !validTo.isBefore(validFrom);
    }
}
