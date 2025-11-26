package com.microservicios.marketplace_ms.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.microservicios.marketplace_ms.entities.Item;
import com.microservicios.marketplace_ms.services.ItemService;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import jakarta.validation.constraints.NotBlank;

@Controller
public class ItemGraphQLController {

    @Autowired
    private ItemService itemService;

    @QueryMapping
    public List<Item> itemsPorClasificacion(
            @Argument @NotBlank(message = "La clasificación es obligatoria") String clasificacion) {
        System.out.println("GraphQL: Llamando a itemsPorClasificacion con: " + clasificacion);
        List<Item> result = itemService.getItemsPorClasificacion(clasificacion);
        System.out.println("GraphQL: Retornando " + result.size() + " items");
        return result;
    }

    @ExceptionHandler(Exception.class)
    public GraphQLError handleException(Exception ex) {
        System.out.println("GraphQL Exception: " + ex.getMessage());
        return GraphqlErrorBuilder.newError()
                .message("Error interno: " + ex.getMessage())
                .build();
    }
}