package com.celotts.purchaseservice.application.usecase;

import com.celotts.purchaseservice.domain.exception.PurchaseNotFoundException;
import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import com.celotts.purchaseservice.domain.port.input.PurchaseUseCase;
import com.celotts.purchaseservice.domain.port.output.PurchaseRepositoryPort;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante para readOnly

import java.util.List; // Faltaba este import
import java.util.UUID;

@Service
@RequiredArgsConstructor
// CAMBIO: Se usa 'implements' porque PurchaseUseCase es una interface
public class PurchaseUseCaseImpl implements PurchaseUseCase {

    private final PurchaseRepositoryPort repositoryPort;

    @Override
    @Transactional
    public PurchaseModel create(PurchaseModel purchase) {
        purchase.normalize();
        return repositoryPort.save(purchase);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseModel findById(UUID id) {
        return repositoryPort.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException("purchase.not-found-with-id" + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseModel> findAll() {
        return repositoryPort.findAll();
    }

    @Override
    @Transactional
    public PurchaseModel update(UUID id, PurchaseModel purchase) {
        return repositoryPort.findById(id)
                .map(existingPurchase -> {
                    purchase.setId(id);
                    purchase.normalize();
                    return repositoryPort.save(purchase);
                })
                .orElseThrow(() -> new PurchaseNotFoundException("purchase.cannot-update-not-found" + id));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        PurchaseModel purchase = repositoryPort.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException("purchase.cannot-delete-not-found" + id));

        repositoryPort.deleteById(purchase.getId());
    }
}