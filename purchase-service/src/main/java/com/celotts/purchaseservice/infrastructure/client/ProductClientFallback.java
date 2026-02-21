package com.celotts.purchaseservice.infrastructure.client;

import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.StockReceptionDto;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.product.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class ProductClientFallback implements ProductClient {

    @Override
    public ProductDto getProductById(UUID id) {
        log.error("⚠️ Error al conectar con Product Service para obtener producto: {}", id);
        // Retornamos null o lanzamos una excepción de negocio personalizada que no sea FeignException
        // Para este caso, null indica que no se pudo validar, lo que debería detener la compra controladamente.
        return null; 
    }

    @Override
    public Map<String, Object> receiveStock(StockReceptionDto receptionDto) {
        log.error("⚠️ Error al conectar con Product Service para recibir stock del producto: {}", receptionDto.getProductId());
        // Retornamos un mapa indicando error para que el servicio de compras sepa que falló la actualización de inventario
        return Map.of("error", "No se pudo actualizar el stock en Product Service. Servicio no disponible.");
    }
}
