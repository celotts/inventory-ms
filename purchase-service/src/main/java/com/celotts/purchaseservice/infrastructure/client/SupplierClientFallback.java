package com.celotts.purchaseservice.infrastructure.client;

import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.supplier.SupplierDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class SupplierClientFallback implements SupplierClient {

    @Override
    public SupplierDto getSupplier(UUID id) {
        log.error("⚠️ Error al conectar con Supplier Service para obtener proveedor: {}", id);
        return null; // Indica fallo en la validación
    }

    @Override
    public SupplierDto createdSupplier(SupplierDto supplierDto) {
        log.error("⚠️ Error al conectar con Supplier Service para crear proveedor: {}", supplierDto.getName());
        return null; // Indica que no se pudo crear
    }
}
