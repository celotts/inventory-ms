package com.celotts.purchaseservice.infrastructure.client;

import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.supplier.SupplierDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "supplier-service", fallback = SupplierClientFallback.class)
public interface SupplierClient {

    @GetMapping("/api/v1/suppliers/{id}")
    SupplierDto getSupplier(@PathVariable("id") UUID id);

    @PostMapping("/api/v1/suppliers")
    SupplierDto createdSupplier(@RequestBody SupplierDto supplierDto);
}
