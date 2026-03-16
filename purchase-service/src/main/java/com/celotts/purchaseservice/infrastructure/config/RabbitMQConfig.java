package com.celotts.purchaseservice.infrastructure.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "inventory.events";

    @Bean
    public TopicExchange inventoryEventsExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }
}
