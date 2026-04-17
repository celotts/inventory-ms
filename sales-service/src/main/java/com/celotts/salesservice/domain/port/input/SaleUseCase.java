package com.celotts.salesservice.domain.port.input;

import com.celotts.salesservice.domain.model.SaleModel;
import java.util.List;
import java.util.UUID;

public interface SaleUseCase {
    SaleModel createSale(SaleModel sale);
    SaleModel findById(UUID id);
    List<SaleModel> findAll();
    void completeSale(UUID id);
}