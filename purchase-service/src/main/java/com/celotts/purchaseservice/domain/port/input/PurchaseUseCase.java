package com.celotts.purchaseservice.domain.port.input;

import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PurchaseUseCase {
    PurchaseModel create(PurchaseModel purchase);
    PurchaseModel findById(UUID id); // Devuelve el modelo directamente

    Page<PurchaseModel> findAll(Pageable pageable);

    PurchaseModel update(UUID id, PurchaseModel purchase);
    void delete(UUID id);
}
