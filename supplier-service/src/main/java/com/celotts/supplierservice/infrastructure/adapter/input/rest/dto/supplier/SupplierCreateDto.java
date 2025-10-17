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
public class SupplierCreateDto{

    @NotBlank(message = "Supplier code is required")
            @Pattern(
                    regexp = "^[A-Z0-9\\-_]{3,40}$",
                    message = "Code must be 3 - 40 character long and contain only uppercase letters, number, hyphens, and underscores"
            )
    String code;

    @NotBlank(message = "Supplier name  is required")
    @Size(min = 2, max =15, message = "Name must be between 2 and 150 characters")
    private String name;

    @Size(max = 30, message = "Tax ID must not exceed 30 characters")
    @JsonProperty("tax_id")
    private String taxId;

    @Email(message = "Invalid email format")
    @Size(max = 120, message = "Email must not exceed 120 characters")
    private String email;

    @Size(max = 40, message = "Phone must not exceed 40 character")
    private String phone;

    private String address;

    @Builder.Default
    private Boolean enabled = true;

    public void trimFields() {
        if (name != null) {
            name = name.trim();
        }
    }



}
