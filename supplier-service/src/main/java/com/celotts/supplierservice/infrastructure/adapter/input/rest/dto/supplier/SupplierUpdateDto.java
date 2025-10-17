package com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierUpdateDto {

    @Pattern(
            regexp = "^[A-Z0-9\\-_]{3,40}$",
            message = "Code must be 3–40 characters long and contain only uppercase letters, numbers, hyphens, and underscores"
    )
    @NotBlank(message = "Supplier code cannot be blank")
    private String code;

    @Size(min = 2, max = 150, message = "Name must be between 2 and 150 characters")
    private String name;

    @Size(max = 30, message = "Tax ID must not exceed 30 characters")
    @JsonProperty("tax_id")
    private String taxId;

    @Email(message = "Invalid email format")
    @Size(max = 120, message = "Email must not exceed 120 characters")
    private String email;

    @Size(max = 40, message = "Phone must not exceed 40 characters")
    private String phone;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    @NotNull(message = "Enabled field cannot be null")
    @JsonProperty("active")
    private Boolean enabled;

    public SupplierUpdateDto() { }

    public void normalizeFields() {
        if (name != null) name = name.trim();
        if (email != null) email = email.trim().toLowerCase();
        if (code != null) code = code.trim().toUpperCase();
        if (taxId != null) taxId = taxId.trim().toUpperCase();
        if (phone != null) phone = phone.trim();
        if (address != null) address = address.trim();
    }
}