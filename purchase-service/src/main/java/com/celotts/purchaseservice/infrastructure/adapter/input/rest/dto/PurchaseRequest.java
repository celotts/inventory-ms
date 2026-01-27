package com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequest {

    @NotBlank(message = "{purchase.code.required}")
    @Size(min = 3, max = 40, message = "{purchase.code.size}")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "{purchase.code.pattern}")
    private String code;

    @NotBlank(message = "{purchase.name.required}")
    @Size(min = 2, max = 150, message = "{purchase.name.size}")
    private String name;

    @Size(max = 30, message = "{purchase.taxid.size}")
    private String taxId;

    @Email(message = "{purchase.email.format}")
    @Size(max = 120, message = "{purchase.email.size}")
    private String email;

    @Size(max = 40, message = "{purchase.phone.size}")
    private String phone;

    @Size(max = 255, message = "{purchase.address.size}")
    private String address;

    private Boolean enabled;
}