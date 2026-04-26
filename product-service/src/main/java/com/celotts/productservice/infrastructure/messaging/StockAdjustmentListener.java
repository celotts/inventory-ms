package com.celotts.productservice.infrastructure.messaging;

import com.celotts.productservice.domain.port.input.product.ProductUseCase;
import com.celotts.productservice.infrastructure.config.RabbitMQConfig;
import com.celotts.productservice.infrastructure.messaging.dto.StockAdjustmentEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockAdjustmentListener {

    private final ObjectMapper objectMapper;
    private final ProductUseCase productUseCase;

    @SneakyThrows
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleStockAdjustment(String message) {
        log.info("Received stock adjustment message: {}", message);

        try {
            StockAdjustmentEvent event = objectMapper.readValue(message, StockAdjustmentEvent.class);

            if (event == null || event.getItems() == null) {
                log.warn("Received a malformed or empty stock adjustment event.");
                return;
            }

            // Process each item in the event
            for (StockAdjustmentEvent.StockAdjustmentItem item : event.getItems()) {
                try {
                    productUseCase.adjustStock(item.getProductId(), item.getQuantity());
                    log.info("Stock for product {} adjusted by {}.", item.getProductId(), item.getQuantity());
                } catch (Exception e) {
                    // Individual item processing error
                    log.error("Failed to process stock adjustment for product {}: {}", item.getProductId(), e.getMessage());
                    // Here you could send the failed item to a dead-letter-queue (DLQ) for manual review
                }
            }
        } catch (Exception e) {
            // General message processing error
            log.error("Failed to process entire stock adjustment message: {}", e.getMessage());
            // This message will be requeued or sent to DLQ based on broker configuration
            throw e; // Re-throw to trigger retry/DLQ mechanism
        }
    }
}
