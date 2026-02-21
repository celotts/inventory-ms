package com.celotts.purchaseservice.infrastructure.client;

import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.StockReceptionDto;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.product.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "product-service", fallback = ProductClientFallback.class)
public interface ProductClient {

    @GetMapping("/api/v1/products/{id}")
    ProductDto getProductById(@PathVariable("id") UUID id);

    @PostMapping("/api/v1/products/receive-stock")
    Map<String, Object> receiveStock(@RequestBody StockReceptionDto receptionDto);
}
