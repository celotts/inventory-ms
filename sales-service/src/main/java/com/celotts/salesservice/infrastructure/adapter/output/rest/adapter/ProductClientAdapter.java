package com.celotts.salesservice.infrastructure.adapter.output.rest.adapter;

import com.celotts.salesservice.domain.port.output.ProductClientPort;
import com.celotts.salesservice.infrastructure.adapter.output.rest.client.ProductFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductClientAdapter implements ProductClientPort {

    private final ProductFeignClient productFeignClient;

    @Override
    public void discountStock(UUID productId, BigDecimal quantity, String reference) {
        try {
            log.info("Llamando a product-service para descontar {} del producto {}", quantity, productId);
            productFeignClient.consumeStock(productId, quantity, reference, "sales-service");
            log.info("Descuento de stock exitoso para referencia {}", reference);
        } catch (Exception e) {
            log.error("Error al descontar stock para el producto {}: {}", productId, e.getMessage());
            // Aquí podrías lanzar una excepción de negocio si el stock es crítico
        }
    }

    @Override
    public boolean checkStock(UUID productId, BigDecimal quantity) {
        // Implementar validación de stock si el product-service expone un endpoint de validación
        return true; 
    }
}