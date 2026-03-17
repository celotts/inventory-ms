package com.celotts.productservice.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "inventory.events";
    public static final String QUEUE_NAME = "product.stock.adjustment.queue";
    public static final String ROUTING_KEY = "stock.adjustment.#";

    @Bean
    public TopicExchange inventoryEventsExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue stockAdjustmentQueue() {
        return new Queue(QUEUE_NAME, true); // durable=true
    }

    @Bean
    public Binding stockAdjustmentBinding(Queue stockAdjustmentQueue, TopicExchange inventoryEventsExchange) {
        return BindingBuilder.bind(stockAdjustmentQueue)
                .to(inventoryEventsExchange)
                .with(ROUTING_KEY);
    }
}
