package com.microservicios.transaction_ms.messagingrabbitmq.components;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String TOPIC_EXCHANGE_NAME = "transaction-exchange";
    public static final String QUEUE_PROCESS_PAYMENT = "process-payment";
    public static final String QUEUE_ADD_TO_CART = "add-to-cart";
    public static final String QUEUE_PAYMENT_CONFIRMATION = "payment-confirmation";

    @Bean
    Queue processPaymentQueue() {
        return new Queue(QUEUE_PROCESS_PAYMENT, false);
    }

    @Bean
    Queue paymentConfirmationQueue() {
        return new Queue(QUEUE_PAYMENT_CONFIRMATION, false);
    }

    @Bean
    Queue addToCartQueue() {
        return new Queue(QUEUE_ADD_TO_CART, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    Binding addToCartBinding(Queue addToCartQueue, TopicExchange exchange) {
        return BindingBuilder.bind(addToCartQueue).to(exchange).with("cart.add");
    }

    @Bean
    Binding bindingProcessPayment(Queue processPaymentQueue, TopicExchange exchange) {
        return BindingBuilder.bind(processPaymentQueue).to(exchange).with("payment.process");
    }

    @Bean
    Binding bindingPaymentConfirmation(Queue paymentConfirmationQueue, TopicExchange exchange) {
        return BindingBuilder.bind(paymentConfirmationQueue).to(exchange).with("transaction.payment-confirmation");
    }
}