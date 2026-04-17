package com.celotts.salesservice.domain.port.output;

import com.celotts.salesservice.domain.model.SaleModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SaleRepositoryPort {
    SaleModel save(SaleModel sale);
    Optional<SaleModel> findById(UUID id);
    List<SaleModel> findAll();
}