package com.celotts.purchaseservice.infrastructure.client;

import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.product.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "product-service")
public interface ProductClient {

    /**
     * Calls the product-service to get a product by its ID.
     * Feign will automatically handle the 404 case by throwing a FeignException.NotFound,
     * which can be caught and re-thrown as a domain-specific exception.
     *
     * @param id The UUID of the product to retrieve.
     * @return A DTO with the product's information.
     */
    @GetMapping("/api/v1/products/{id}")
    ProductDto getProductById(@PathVariable("id") UUID id);
}
