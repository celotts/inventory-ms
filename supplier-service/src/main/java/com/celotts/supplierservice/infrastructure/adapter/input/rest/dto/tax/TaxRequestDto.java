package com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.tax;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaxRequestDto {

    @NotBlank(message = "{tax.code.required}")
    @Size(min = 3, max = 40, message = "{tax.code.size}")
    @Pattern(
            regexp = "^[A-Z0-9\\-_]{3,40}$",
            message = "{tax.code.pattern}"
    )
    private String code;

    @NotBlank(message = "{tax.name.required}")
    @Size(min = 2, max = 120, message = "{tax.name.size}")
    private String name;

    @NotNull(message = "{tax.rate.required}")
    @DecimalMin(value = "0.00", inclusive = true, message = "{tax.rate.min}")
    @DecimalMax(value = "100.00", inclusive = true, message = "{tax.rate.max}")
    @Digits(integer = 3, fraction = 2, message = "{tax.rate.digits}")
    private BigDecimal rate; // porcentaje 0.00..100.00

    @Size(max = 60, message = "{tax.jurisdiction.size}")
    private String jurisdiction; // p.ej. MX

    @Size(max = 30, message = "{tax.type.size}")
    @JsonProperty("tax_type")
    private String taxType;      // p.ej. VAT, IEPS

    @PastOrPresent(message = "{tax.validfrom.pastOrPresent}")
    @Builder.Default
    @JsonProperty("valid_from")
    private LocalDate validFrom = LocalDate.now();

    @FutureOrPresent(message = "{tax.validto.futureOrPresent}")
    @JsonProperty("valid_to")
    private LocalDate validTo;   // null = vigente

    @Builder.Default
    @JsonProperty("active")
    private Boolean enabled = true;

    /**
     * Normalize all String fields to standard format:
     * - code: UPPERCASE
     * - name: trim & collapse spaces (case preserved)
     * - jurisdiction: UPPERCASE
     * - taxType: UPPERCASE
     * (rate/fechas no se normalizan aquí)
     */
    public void normalizeFields() {
        this.code         = normalizeString(code);
        this.name         = collapseSpaces(name);
        this.jurisdiction = normalizeString(jurisdiction);
        this.taxType      = normalizeString(taxType);
    }

    /**
     * Utility method to safely normalize a string field.
     *
     * @param value the string to normalize
     * @return normalized string or null if input is null/blank
     */
    private String normalizeString(String value) {
        if (value == null || value.isBlank()) return null;
        String trimmed = value.trim().replaceAll("\\s+", " ");
        return trimmed.toUpperCase();
    }

    private String collapseSpaces(String value) {
        if (value == null || value.isBlank()) return null;
        String t = value.trim().replaceAll("\\s+", " ");
        return t.isEmpty() ? null : t;
    }
}