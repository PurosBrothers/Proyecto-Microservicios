package com.microservicios.transaction_ms.messagingrabbitmq.components;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String TOPIC_EXCHANGE_NAME = "c";
    public static final String QUEUE_PROCESS_PAYMENT = "process-payment";

    @Bean
    Queue processPaymentQueue() {
        return new Queue(QUEUE_PROCESS_PAYMENT, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    Binding binding(Queue processPaymentQueue, TopicExchange exchange) {
        return BindingBuilder.bind(processPaymentQueue).to(exchange).with("payment.process");
    }
}