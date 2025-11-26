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
            System.out.println("Intentando enviar mensaje a RabbitMQ (payment-confirmation)...");
            System.out.println("Host configurado: " + rabbitTemplate.getConnectionFactory().getHost());
            System.out.println("Puerto configurado: " + rabbitTemplate.getConnectionFactory().getPort());
            rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE_NAME, "transaction.payment-confirmation",
                    message);
            System.out.println("Mensaje de confirmación enviado: " + message);
        } catch (JsonProcessingException e) {
            System.err.println("Error enviando mensaje de confirmación: " + e.getMessage());
            if (rabbitTemplate.getConnectionFactory() != null) {
                System.err.println("Host de conexión: " + rabbitTemplate.getConnectionFactory().getHost());
                System.err.println("Puerto de conexión: " + rabbitTemplate.getConnectionFactory().getPort());
            }
            e.printStackTrace();
        }
    }
}