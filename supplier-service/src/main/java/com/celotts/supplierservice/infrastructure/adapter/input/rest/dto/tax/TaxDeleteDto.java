package com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.tax;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaxDeleteDto {

    @NotNull(message = "{tax.delete.id.required}")
    UUID id;

    @Size(max = 100, message = "{tax.delete.deletedBy.size}")
    @Pattern(
            regexp = "^[\\p{L}0-9._\\-\\s@]+$",
            message = "{tax.delete.deletedBy.pattern}"
    )
    String deletedBy;

    @Size(max = 255, message ="{tax.delete.reason.size}")
    String reason;

    public TaxDeleteDto normalizeFields() {
        return this.toBuilder()
                .deletedBy(normalize(deletedBy))
                .reason(normalize(reason))
                .build();
    }

    public static String normalize(String value) {
        if (value == null) return null;
        String normalized = value.trim().replaceAll("\\s+", " ");
        return normalized.isEmpty() ? null : normalized;
    }
}
