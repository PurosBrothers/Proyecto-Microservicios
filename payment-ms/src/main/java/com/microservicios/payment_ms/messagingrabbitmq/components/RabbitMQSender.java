package com.microservicios.payment_ms.messagingrabbitmq.components;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicios.payment_ms.dtos.PaymentConfirmationMessageDTO;

@Service
public class RabbitMQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendPaymentConfirmationMessage(PaymentConfirmationMessageDTO messageDTO) {
        try {
            String message = objectMapper.writeValueAsString(messageDTO);
            rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE_NAME, "transaction.payment-confirmation",
                    message);
            System.out.println("Mensaje de confirmación enviado: " + message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}