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

    public void sendAddToCartMessage(Object message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE_NAME, "cart.add", jsonMessage);
            System.out.println("Mensaje enviado a add-to-cart: " + jsonMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}