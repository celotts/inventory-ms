package com.celotts.purchaseservice.infrastructure.adapter.output.feign;

import com.celotts.purchaseservice.infrastructure.adapter.output.feign.dto.SupplierDto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "supplier-service",
        contextId = "supplier-main-client"
)
public interface SupplierClient {
    @GetMapping("/api/v1/suppliers/{id}")
    SupplierDto getSupplier(@PathVariable("id") UUID id);
}