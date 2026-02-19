package com.celotts.purchaseservice.infrastructure.client;

import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.tax.TaxDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class TaxClientFallback implements TaxClient {

    @Override
    public TaxDto getTaxById(UUID id) {
        log.error("⚠️ Error al conectar con Tax Service para obtener impuesto: {}", id);
        return null;
    }
}
