package com.celotts.purchaseservice.domain.port.input;

import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;

import java.util.UUID;

public interface ReceivePurchaseUseCase {
    PurchaseModel receive(UUID purchaseId);
}
