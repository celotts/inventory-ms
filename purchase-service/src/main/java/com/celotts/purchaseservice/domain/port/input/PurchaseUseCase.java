package com.celotts.purchaseservice.domain.port.input;

import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel; // Se agregó el punto y coma faltante

import java.util.List;
import java.util.UUID;

// CAMBIO: Se cambió 'public class' por 'public interface'
public interface PurchaseUseCase {
    PurchaseModel create(PurchaseModel purchase);
    PurchaseModel findById(UUID id);
    List<PurchaseModel> findAll();
    PurchaseModel update(UUID id, PurchaseModel purchase);
    void delete(UUID id);
}