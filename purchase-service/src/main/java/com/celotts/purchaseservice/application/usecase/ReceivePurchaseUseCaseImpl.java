package com.celotts.purchaseservice.application.usecase;

import com.celotts.purchaseservice.domain.exception.InvalidPurchaseStateException;
import com.celotts.purchaseservice.domain.exception.PurchaseNotFoundException;
import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import com.celotts.purchaseservice.domain.port.input.ReceivePurchaseUseCase;
import com.celotts.purchaseservice.domain.port.output.PurchaseRepositoryPort;
import com.celotts.purchaseservice.infrastructure.adapter.output.persistence.outbox.OutboxEventEntity;
import com.celotts.purchaseservice.infrastructure.adapter.output.persistence.outbox.OutboxEventRepository;
import com.celotts.purchaseservice.infrastructure.messaging.dto.StockAdjustmentEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List; // Import añadido
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReceivePurchaseUseCaseImpl implements ReceivePurchaseUseCase {

    private final PurchaseRepositoryPort repositoryPort;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

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

        // 2. Create Outbox Event (instead of direct call)
        createStockAdjustmentEvent(purchase);

        // 3. Update Purchase State
        purchase.setStatus("RECEIVED");
        purchase.setReceivedAt(LocalDateTime.now());
        purchase.setUpdatedAt(LocalDateTime.now());

        return repositoryPort.save(purchase);
    }

    @SneakyThrows // Handles ObjectMapper checked exception
    private void createStockAdjustmentEvent(PurchaseModel purchase) {
        List<StockAdjustmentEvent.StockAdjustmentItem> eventItems = purchase.getItems().stream()
                .map(item -> StockAdjustmentEvent.StockAdjustmentItem.builder()
                        .productId(item.getProductId())
                        .quantity(item.getQuantity().intValue()) // Assuming quantity can be converted to int
                        .build())
                .collect(Collectors.toList());

        StockAdjustmentEvent eventPayload = StockAdjustmentEvent.builder()
                .eventId(UUID.randomUUID())
                .purchaseId(purchase.getId())
                .reason("PURCHASE_RECEIVED")
                .items(eventItems)
                .build();

        OutboxEventEntity outboxEvent = OutboxEventEntity.builder()
                .aggregateType("PURCHASE")
                .aggregateId(purchase.getId())
                .eventType("STOCK_ADJUSTMENT_REQUESTED")
                .payload(objectMapper.writeValueAsString(eventPayload))
                .build();

        outboxEventRepository.save(outboxEvent);
        log.info("Outbox event created for stock adjustment. Aggregate ID: {}", purchase.getId());
    }
}
