package com.celotts.purchaseservice.infrastructure.adapter.output.feign.config;

import com.celotts.purchaseservice.domain.exception.SupplierNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        // Si el micro de proveedores responde 404 Not Found
        if (response.status() == 404) {
            return new SupplierNotFoundException("supplier.not-found", "id", "unknown");
        }

        // Para cualquier otro error (500, 400, etc.), deja que Feign lo maneje normalmente
        return feign.FeignException.errorStatus(methodKey, response);
    }
}