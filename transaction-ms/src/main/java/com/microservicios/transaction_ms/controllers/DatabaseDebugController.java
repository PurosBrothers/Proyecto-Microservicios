package com.microservicios.transaction_ms.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/debug")
public class DatabaseDebugController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/tables")
    public Mono<List<String>> getTables() {
        return Mono.fromCallable(() -> jdbcTemplate.queryForList(
                "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC'",
                String.class));
    }

    @GetMapping("/table/{tableName}")
    public Mono<List<Map<String, Object>>> getTableData(@PathVariable String tableName) {
        return Mono.fromCallable(() -> jdbcTemplate.queryForList("SELECT * FROM " + tableName));
    }

    @GetMapping("/carrito-compra")
    public Mono<List<Map<String, Object>>> getCarritoCompra() {
        return Mono.fromCallable(() -> jdbcTemplate.queryForList("SELECT * FROM carrito_compra"));
    }
}