package com.celotts.purchaseservice.application.usecase;

import com.celotts.purchaseservice.domain.exception.InvalidPurchaseStateException;
import com.celotts.purchaseservice.domain.exception.PurchaseNotFoundException;
import com.celotts.purchaseservice.domain.exception.ServiceUnavailableException;
import com.celotts.purchaseservice.domain.model.purchase.PurchaseItemModel;
import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import com.celotts.purchaseservice.domain.port.input.ReceivePurchaseUseCase;
import com.celotts.purchaseservice.domain.port.output.PurchaseRepositoryPort;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.StockReceptionDto;
import com.celotts.purchaseservice.infrastructure.client.ProductClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReceivePurchaseUseCaseImpl implements ReceivePurchaseUseCase {

    private final PurchaseRepositoryPort repositoryPort;
    private final ProductClient productClient;
    private final MessageSource messageSource;

    private String getMsg(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    @Override
    @Transactional
    public PurchaseModel receive(UUID purchaseId) {
        log.info("Receiving purchase: {}", purchaseId);

        PurchaseModel purchase = repositoryPort.findById(purchaseId)
                .orElseThrow(() -> new PurchaseNotFoundException("purchase.not-found", purchaseId));

        // 1. Validate State
        if (!"PLACED".equalsIgnoreCase(purchase.getStatus()) && !"DRAFT".equalsIgnoreCase(purchase.getStatus())) {
            throw new InvalidPurchaseStateException("purchase.invalid-state-for-receive", purchase.getStatus());
        }

        // 2. Process Items
        for (PurchaseItemModel item : purchase.getItems()) {
            StockReceptionDto receptionDto = StockReceptionDto.builder()
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .unitCost(item.getUnitCost())
                    .referenceNumber(purchase.getOrderNumber())
                    .supplierId(purchase.getSupplierId())
                    .build();

            try {
                productClient.receiveStock(receptionDto);
                log.info("Stock received for item: {}", item.getProductId());
            } catch (Exception e) {
                log.error("Failed to receive stock for item {}: {}", item.getProductId(), e.getMessage());
                throw new ServiceUnavailableException("service.product.stock-update-failed");
            }
        }

        // 3. Update Purchase State
        purchase.setStatus("RECEIVED");
        purchase.setReceivedAt(LocalDateTime.now());
        purchase.setUpdatedAt(LocalDateTime.now());

        return repositoryPort.save(purchase);
    }
}
