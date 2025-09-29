package com.microservicios.marketplace_ms.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.microservicios.marketplace_ms.entities.Item;
import com.microservicios.marketplace_ms.services.ItemService;

@Controller
public class ItemGraphQLController {

    @Autowired
    private ItemService itemService;

    @QueryMapping
    public List<Item> itemsPorClasificacion(@Argument String clasificacion) {
        return itemService.getItemsPorClasificacion(clasificacion);
    }
}