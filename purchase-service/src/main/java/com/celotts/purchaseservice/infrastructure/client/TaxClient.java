package com.celotts.purchaseservice.infrastructure.client;

import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.tax.TaxDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "tax-service", fallback = TaxClientFallback.class)
public interface TaxClient {

    @GetMapping("/api/v1/taxes/{id}")
    TaxDto getTaxById(@PathVariable("id") UUID id);
}
