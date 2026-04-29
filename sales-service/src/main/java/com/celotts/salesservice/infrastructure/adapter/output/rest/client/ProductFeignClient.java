package com.celotts.salesservice.infrastructure.adapter.output.rest.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "product-service", url = "${app.clients.product-service.url:http://product-service:9090}")
public interface ProductFeignClient {

    @PostMapping("/api/v1/movements/consume")
    void consumeStock(@RequestParam UUID productId,
                      @RequestParam BigDecimal quantity,
                      @RequestParam String reference,
                      @RequestParam String user);
}