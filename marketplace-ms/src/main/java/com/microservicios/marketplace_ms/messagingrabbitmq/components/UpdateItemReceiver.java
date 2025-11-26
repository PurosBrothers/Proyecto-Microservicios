package com.microservicios.marketplace_ms.messagingrabbitmq.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicios.marketplace_ms.services.ItemService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateItemReceiver {

    @Autowired
    private ItemService itemService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "update-item")
    public void receiveUpdateItem(String message) {
        System.out.println("Mensaje update-item recibido: " + message);
        try {
            // Asumir que el DTO es similar, pero como no está compartido, parsear
            // manualmente o crear DTO local
            // Por simplicidad, parsear como Map o crear DTO local
            // Aquí asumiré que el mensaje es JSON con itemId, cantidadVendida, tipoCambio
            var jsonNode = objectMapper.readTree(message);
            Long itemId = jsonNode.get("itemId").asLong();
            Integer cantidadVendida = jsonNode.get("cantidadVendida").asInt();
            String tipoCambio = jsonNode.get("tipoCambio").asText();

            // Llamar a servicio para actualizar item
            itemService.updateItemStock(itemId, cantidadVendida, tipoCambio);
            System.out.println("Item actualizado: " + itemId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}