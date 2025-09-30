package com.microservicios.marketplace_ms.messagingrabbitmq.components;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String TOPIC_EXCHANGE_NAME = "transaction-exchange";
    public static final String QUEUE_UPDATE_ITEM = "marketplace.update-item";

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    Queue updateItemQueue() {
        return new Queue(QUEUE_UPDATE_ITEM);
    }

    @Bean
    Binding updateItemBinding(Queue updateItemQueue, TopicExchange exchange) {
        return BindingBuilder.bind(updateItemQueue).to(exchange).with("marketplace.update-item");
    }
}