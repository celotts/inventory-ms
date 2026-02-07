package com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.supplier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // <--- SOLUCIÓN DEFINITIVA
public class SupplierDto {
    private UUID id;
    private String code;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String category;
    private boolean active;
}
