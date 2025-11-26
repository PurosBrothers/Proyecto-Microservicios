package com.microservicios.marketplace_ms.messagingrabbitmq.components;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RabbitMQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendAddToCartMessage(Object message) throws Exception {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            System.out.println("Intentando enviar mensaje a RabbitMQ...");
            System.out.println("Host configurado: " + rabbitTemplate.getConnectionFactory().getHost());
            System.out.println("Puerto configurado: " + rabbitTemplate.getConnectionFactory().getPort());
            rabbitTemplate.convertAndSend(RabbitMQMessagingConfig.TOPIC_EXCHANGE_NAME, "cart.add", jsonMessage);
            System.out.println("Mensaje enviado a add-to-cart: " + jsonMessage);
        } catch (Exception e) {
            System.err.println("Error al enviar mensaje a RabbitMQ: " + e.getMessage());
            if (rabbitTemplate.getConnectionFactory() != null) {
                System.err.println("Host de conexión: " + rabbitTemplate.getConnectionFactory().getHost());
                System.err.println("Puerto de conexión: " + rabbitTemplate.getConnectionFactory().getPort());
            }
            e.printStackTrace();
            throw e; // Re-throw to let caller handle it
        }
    }
}