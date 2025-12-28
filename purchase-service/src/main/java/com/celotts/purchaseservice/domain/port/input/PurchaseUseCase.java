package com.celotts.purchaseservice.domain.port.input;

import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import org.springframework.data.domain.Page; // Necesario para paginación
import org.springframework.data.domain.Pageable; // Necesario para parámetros

import java.util.Optional;
import java.util.UUID;

public interface PurchaseUseCase {
    PurchaseModel create(PurchaseModel purchase);
    Optional<PurchaseModel> findById(UUID id);

    Page<PurchaseModel> findAll(Pageable pageable);

    PurchaseModel update(UUID id, PurchaseModel purchase);
    void delete(UUID id);
}