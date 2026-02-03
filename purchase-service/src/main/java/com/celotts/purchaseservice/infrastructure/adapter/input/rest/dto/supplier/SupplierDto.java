package com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.supplier;

import lombok.Data;

@Data
public class SupplierDto {
    private Long id;
    private String name;        // Nombre de la empresa o proveedor
    private String email;       // Para notificaciones de compra
    private String category;    // Ej: "Electrónicos", "Alimentos"
    private boolean active;     // ¡Importante! No deberías comprarle a alguien inactivo
}
