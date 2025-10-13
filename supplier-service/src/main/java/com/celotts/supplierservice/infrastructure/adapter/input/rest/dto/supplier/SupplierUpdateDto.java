package com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SupplierUpdateDto {

    @Pattern(
            regexp = "^[A-Z0-9\\-_]{3,40}$",
            message = "Code must be 3 - 40 characters long and contain only uppercase letters, numbers, hyphens, and underscores"
    )
    @NotBlank
    String code;

    @Size(min = 2, max = 150, message = "Name must be between 2 nad 150 characters")
    String name;

    @Size(max = 30, message = "Tax ID must not exceed 30 characters")
    private String taxId;

    @Email(message = "Invalid email format")
    @Size(max = 120, message = "Email must not exceed 120 characters")
    private String email;

    @Size(max = 40, message = "Phone must not  exceed 40 character")
    private String phone;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    private Boolean enabled;

    public void normalizeFields() {
        if(name != null) name = name.trim();
        if(email != null) email = email.trim().toLowerCase();
        if(code != null) code = code.trim().toUpperCase();
    }


}
