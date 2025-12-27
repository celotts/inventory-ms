package com.celotts.purchaseservice.domain.port.output;

import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel; // Importar el modelo aquí

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseRepositoryPort {
    PurchaseModel save(PurchaseModel purchase);
    Optional<PurchaseModel> findById(UUID id);
    Optional<PurchaseModel> findByOrderNumber(String orderNumber);

    List<com.celotts.purchaseservice.domain.model.purchase.PurchaseModel> findAll();

    void deleteById(UUID id);
}