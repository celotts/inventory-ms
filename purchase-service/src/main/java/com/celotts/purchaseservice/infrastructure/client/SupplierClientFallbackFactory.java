package com.celotts.purchaseservice.infrastructure.client;

import com.celotts.purchaseservice.domain.exception.ServiceUnavailableException;
import com.celotts.purchaseservice.domain.exception.SupplierNotFoundException;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.supplier.SupplierDto;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class SupplierClientFallbackFactory implements FallbackFactory<SupplierClient> {

    @Override
    public SupplierClient create(Throwable cause) {
        return new SupplierClient() {
            @Override
            public SupplierDto getSupplier(UUID id) {
                // Si es un 404 real del servicio remoto
                if (cause instanceof FeignException.NotFound) {
                    log.warn("Supplier not found in remote service: {}", id);
                    throw new SupplierNotFoundException("supplier.not-found", "id", id.toString());
                }
                
                // Para cualquier otro error (conexión, timeout, 500), lanzamos servicio no disponible
                log.error("Error calling Supplier Service for ID {}: {}", id, cause.getMessage());
                throw new ServiceUnavailableException("service.supplier.unavailable");
            }

            @Override
            public SupplierDto createdSupplier(SupplierDto supplierDto) {
                log.error("Error creating supplier: {}", cause.getMessage());
                throw new ServiceUnavailableException("service.supplier.unavailable");
            }
        };
    }
}
