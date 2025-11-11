package com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupplierRequestDto {

    @NotBlank(message = "{supplier.code.required}")
    @Size(min = 3, max = 40, message = "{supplier.code.size}")
    @Pattern(
            regexp = "^[A-Z0-9\\-_]{3,40}$",
            message = "{validation.code.pattern}"
    )
    private String code;

    @NotBlank(message = "{supplier.name.required}")
    @Size(min = 2, max = 150, message = "{supplier.name.size}")
    private String name;

    @Size(max = 30, message = "{supplier.taxid.size}")
    @JsonProperty("tax_id")
    private String taxId;

    @Email(message = "{supplier.email.format}")
    @Size(max = 120, message = "{supplier.email.size}")
    private String email;

    @Size(max = 40, message = "{supplier.phone.size}")
    private String phone;

    @Size(max = 255, message = "{supplier.address.size}")
    private String address;

    @Builder.Default
    private Boolean enabled = true;

    /**
     * Normalize all String fields to standard format:
     * - code: UPPERCASE
     * - name: trim
     * - email: trim and lowercase
     * - phone: trim and uppercase
     * - taxId: trim and uppercase
     * - address: trim
     */
    public void normalizeFields() {
        this.code = normalizeString(code, true);
        this.name = normalizeString(name, false);
        this.taxId = normalizeString(taxId, true);
        this.email = normalizeString(email, false);
        this.phone = normalizeString(phone, true);
        this.address = normalizeString(address, false);
    }

    /**
     * Utility method to safely normalize a string field.
     *
     * @param value the string to normalize
     * @param toUpperCase if true, convert to uppercase; otherwise to lowercase
     * @return normalized string or null if input is null
     */
    private String normalizeString(String value, boolean toUpperCase) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String trimmed = value.trim();
        return toUpperCase ? trimmed.toUpperCase() : trimmed.toLowerCase();
    }
}