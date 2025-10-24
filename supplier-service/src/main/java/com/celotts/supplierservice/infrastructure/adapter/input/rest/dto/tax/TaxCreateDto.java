package com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.tax;

import com.celotts.supplierservice.infrastructure.common.validation.ValidationGroups;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

import jakarta.validation.constraints.Pattern;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxCreateDto {
    /*
      code VARCHAR(30) NOT NULL,              -- 'IVA16'
      name VARCHAR(120) NOT NULL,             -- 'IVA 16% MX'
      rate DECIMAL(5,2) NOT NULL CHECK (rate >= 0 AND rate <= 100),
      jurisdiction VARCHAR(60),               -- 'MX'
      tax_type VARCHAR(30),                   -- 'VAT','IEPS'
      valid_from DATE NOT NULL DEFAULT CURRENT_DATE,
      valid_to DATE,
      enabled BOOLEAN NOT NULL DEFAULT TRUE
    */

    @NotBlank(message = "{tax.code.required}", groups = ValidationGroups.Create.class)
    @Pattern(
            regexp = "^[A-Z0-9\\-_]{3,40}$",
            message = "{tax.code.pattern}",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class}
    )
    private String code;

    @NotBlank(message = "{tax.name.required}", groups = ValidationGroups.Create.class)
    @Size(
            min = 2, max = 120,
            message = "{tax.name.size}",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class}
    )
    private String name;

    @NotNull(message = "{tax.rate.required}", groups = ValidationGroups.Create.class)
    @DecimalMin(value = "0.00", inclusive = true, message = "{tax.rate.min}",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @DecimalMax(value = "100.00", inclusive = true, message = "{tax.rate.max}",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Digits(integer = 3, fraction = 2, message = "{tax.rate.digits}",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private BigDecimal rate; // porcentaje 0.00..100.00

    @Size(max = 60, message = "{tax.jurisdiction.size}",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String jurisdiction; // p.ej. "MX"

    @Size(max = 30, message = "{tax.type.size}",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String taxType; // p.ej. "VAT", "IEPS"

    @Builder.Default
    @PastOrPresent(message = "{tax.validfrom.pastOrPresent}",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private LocalDate validFrom = LocalDate.now();

    @FutureOrPresent(message = "{tax.validto.futureOrPresent}",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private LocalDate validTo; // null = vigente

    @Builder.Default
    @JsonAlias({"active","enabled"})
    private Boolean enabled = true;

    @JsonIgnore
    public void normalizeFields() {
        this.code         = upper(trimToNull(code));
        this.name         = collapseSpaces(trimToNull(name));
        this.jurisdiction = upper(trimToNull(jurisdiction));
        this.taxType      = upper(trimToNull(taxType));
        // rate / fechas no se normalizan aquí
    }

    // -------- helpers ----------
    private static final java.util.regex.Pattern SPACES =
            java.util.regex.Pattern.compile("\\s+");

    private static String trimToNull(String v) {
        if (v == null) return null;
        String t = v.trim();
        return t.isEmpty() ? null : t;
    }

    private static String collapseSpaces(String v) {
        return v == null ? null : SPACES.matcher(v).replaceAll(" ");
    }

    private static String upper(String v) {
        return v == null ? null : v.toUpperCase(Locale.ROOT);
    }
}