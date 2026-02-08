package com.celotts.purchaseservice.domain.port.output;

import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.supplier.SupplierDto;

import java.util.Optional;

public interface SupplierServicePort {

    Optional<SupplierDto> getSupplierById(Long id);
}
