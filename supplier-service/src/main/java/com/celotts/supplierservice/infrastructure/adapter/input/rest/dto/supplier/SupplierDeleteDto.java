package com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupplierDeleteDto {

    @NotNull(message = "{supplier.delete.id.required}")
    UUID id;

    @Size(max = 100, message = "{supplier.delete.deletedBy.size}")
    @Pattern(
            regexp = "^[\\p{L}0-9._\\-\\s@]+$",
            message = "{supplier.delete.deletedBy.pattern}"
    )
    String deletedBy;

    @Size(max = 255, message = "{supplier.delete.reason.size}")
    String reason;

    public void normalizeFields() {
        // Objeto inmutable: normaliza antes de construir (Builder/factory).
    }
}