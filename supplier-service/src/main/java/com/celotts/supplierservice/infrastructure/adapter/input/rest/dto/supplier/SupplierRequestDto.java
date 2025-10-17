package com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupplierRequestDto {

    @NotBlank(message = "Supplier code is required")
    @Pattern(
            regexp = "^[A-Z0-9\\-_]{3,40}$",
            message = "Code must be 3 - 40 characters long and contain only uppercase letters, numbers, hyphens, and underscores"
    )
    private String code;

    @NotBlank(message = "Supplier name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 150 characters")
    private String name;

    @Size(max = 30, message = "Tax ID must not exceed 30 characters")
    private String taxId;

    @Email(message = "Invalid email format")
    @Size(max = 120, message = "Email must not exceed 120 characters")
    private String email;

    @Size(max = 40, message = "Phone must not exceed 40 characters")
    private String phone;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    public void normalizeFields() {
        if(name != null) name = name.trim();
        if(email != null) email = email.trim().toLowerCase();
        if(code != null) code = code.trim().toUpperCase();
    }


}
