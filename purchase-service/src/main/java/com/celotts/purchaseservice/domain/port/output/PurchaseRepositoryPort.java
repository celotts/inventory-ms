package com.celotts.purchaseservice.domain.port.output;

import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseRepositoryPort {
    PurchaseModel save(PurchaseModel purchase);
    Optional<PurchaseModel> findById(UUID id);
    Optional<PurchaseModel> findByOrderNumber(String orderNumber);

    // Este es el que faltaba para que compile el UseCase
    boolean existsById(UUID id);

    boolean existsByOrderNumber(String orderNumber);
    Page<PurchaseModel> findAll(Pageable pageable);
    void deleteById(UUID id);
}