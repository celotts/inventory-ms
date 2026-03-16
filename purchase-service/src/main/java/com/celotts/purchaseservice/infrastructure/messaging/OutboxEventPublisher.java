package com.celotts.purchaseservice.infrastructure.messaging;

import com.celotts.purchaseservice.infrastructure.adapter.output.persistence.outbox.OutboxEventEntity;
import com.celotts.purchaseservice.infrastructure.adapter.output.persistence.outbox.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxEventPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final RabbitTemplate rabbitTemplate;

    // TODO: Make these configurable
    public static final String EXCHANGE_NAME = "inventory.events";
    public static final String ROUTING_KEY = "stock.adjustment.v1";

    @Scheduled(fixedDelay = 10000) // Run every 10 seconds
    @Transactional
    public void publishPendingEvents() {
        List<OutboxEventEntity> pendingEvents = outboxEventRepository.findByStatusOrderByOccurredAtAsc("PENDING");

        if (pendingEvents.isEmpty()) {
            return;
        }

        log.info("Found {} pending events to publish.", pendingEvents.size());

        for (OutboxEventEntity event : pendingEvents) {
            try {
                rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, event.getPayload());
                
                event.setStatus("PUBLISHED");
                event.setPublishedAt(LocalDateTime.now());
                outboxEventRepository.save(event);
                log.info("Event {} published successfully.", event.getId());

            } catch (Exception e) {
                log.error("Failed to publish event {}: {}", event.getId(), e.getMessage());
                event.setStatus("FAILED");
                event.setLastError(e.getMessage());
                outboxEventRepository.save(event);
            }
        }
    }
}
