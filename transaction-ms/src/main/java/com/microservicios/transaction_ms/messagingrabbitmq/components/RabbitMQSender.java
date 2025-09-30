package com.microservicios.transaction_ms.messagingrabbitmq.components;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicios.transaction_ms.dtos.ProcessPaymentMessageDTO;
import com.microservicios.transaction_ms.dtos.UpdateItemMessageDTO;

@Service
public class RabbitMQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendProcessPaymentMessage(ProcessPaymentMessageDTO messageDTO) {
        try {
            String message = objectMapper.writeValueAsString(messageDTO);
            rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE_NAME, "payment.process", message);
            System.out.println("Mensaje enviado a process-payment: " + message);
        } catch (Exception e) {
            System.out.println("Error enviando mensaje a process-payment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendUpdateItemMessage(UpdateItemMessageDTO messageDTO) {
        try {
            String message = objectMapper.writeValueAsString(messageDTO);
            rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE_NAME, "marketplace.update-item", message);
            System.out.println("Mensaje enviado a update-item: " + message);
        } catch (Exception e) {
            System.out.println("Error enviando mensaje a update-item: " + e.getMessage());
            e.printStackTrace();
        }
    }
}