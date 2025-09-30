package com.microservicios.gateway_server.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Filtro global que reemplaza placeholders "uid" en las rutas y en el body JSON
 * con el UID del usuario autenticado
 *
 * Este filtro intercepta todas las peticiones HTTP, obtiene el JWT del contexto
 * de seguridad (ya validado por Spring Security), extrae el subject (UID del
 * usuario),
 * y reemplaza cualquier ocurrencia de "uid" en la path y en el body JSON con el
 * UID real.
 */
@Component
@Slf4j
public class UidReplacementFilter implements GlobalFilter, Ordered {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> securityContext.getAuthentication())
                .filter(authentication -> authentication != null && authentication.isAuthenticated())
                .filter(authentication -> authentication.getPrincipal() instanceof Jwt)
                .map(authentication -> (Jwt) authentication.getPrincipal())
                .flatMap(jwt -> {
                    String uid = jwt.getSubject(); // El subject contiene el UID del usuario

                    if (uid != null && !uid.isEmpty()) {
                        ServerHttpRequest request = exchange.getRequest();
                        String originalPath = request.getPath().value();

                        // Verificar si la path contiene "/uid"
                        boolean pathModified = false;
                        ServerHttpRequest modifiedRequest = request;

                        if (originalPath.contains("/uid")) {
                            // Reemplazar "/uid" con el UID real
                            String newPath = originalPath.replace("/uid", "/" + uid);
                            log.info("🔄 Reemplazando UID en ruta: {} -> {}", originalPath, newPath);

                            modifiedRequest = request.mutate().path(newPath).build();
                            pathModified = true;
                        }

                        // Verificar si el body contiene "uid" (solo para JSON)
                        String contentType = request.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
                        if (contentType != null && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                            return modifyRequestBody(exchange, modifiedRequest, uid, pathModified, chain);
                        } else {
                            // No es JSON, solo modificar path si fue necesario
                            if (pathModified) {
                                ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();
                                return chain.filter(modifiedExchange);
                            }
                        }
                    }

                    // No hay modificaciones necesarias
                    return chain.filter(exchange);
                })
                .switchIfEmpty(chain.filter(exchange)) // Si no hay autenticación JWT, continuar sin modificar
                .onErrorResume(error -> {
                    log.warn("❌ Error en filtro de reemplazo UID, continuando sin modificación: {}",
                            error.getMessage());
                    return chain.filter(exchange);
                });
    }

    private Mono<Void> modifyRequestBody(ServerWebExchange exchange, ServerHttpRequest request,
            String uid, boolean pathModified, GatewayFilterChain chain) {
        return DataBufferUtils.join(request.getBody())
                .flatMap(dataBuffer -> {
                    try {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        DataBufferUtils.release(dataBuffer);

                        String bodyString = new String(bytes, StandardCharsets.UTF_8);

                        if (StringUtils.hasText(bodyString) && bodyString.contains("\"uid\"")) {
                            // Parsear JSON y reemplazar uid
                            JsonNode jsonNode = objectMapper.readTree(bodyString);
                            String modifiedBody = replaceUidInJson(jsonNode, uid);

                            if (!bodyString.equals(modifiedBody)) {
                                log.info("🔄 Reemplazando UID en body JSON");

                                // Crear nuevo body
                                byte[] modifiedBytes = modifiedBody.getBytes(StandardCharsets.UTF_8);
                                Flux<DataBuffer> modifiedBodyFlux = Flux.just(
                                        exchange.getResponse().bufferFactory().wrap(modifiedBytes));

                                // Crear request decorator con el nuevo body
                                ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(request) {
                                    @Override
                                    public Flux<DataBuffer> getBody() {
                                        return modifiedBodyFlux;
                                    }

                                    @Override
                                    public HttpHeaders getHeaders() {
                                        HttpHeaders headers = new HttpHeaders();
                                        headers.putAll(super.getHeaders());
                                        headers.setContentLength(modifiedBytes.length);
                                        return headers;
                                    }
                                };

                                ServerWebExchange modifiedExchange = exchange.mutate().request(decorator).build();
                                return chain.filter(modifiedExchange);
                            }
                        }

                        // Body no modificado, pero path sí
                        if (pathModified) {
                            ServerWebExchange modifiedExchange = exchange.mutate().request(request).build();
                            return chain.filter(modifiedExchange);
                        }

                    } catch (IOException e) {
                        log.warn("❌ Error procesando body JSON: {}", e.getMessage());
                    }

                    return chain.filter(exchange);
                });
    }

    private String replaceUidInJson(JsonNode jsonNode, String uid) {
        try {
            // Crear una copia modificable del JSON
            JsonNode modifiedNode = replaceUidInJsonNode(jsonNode, uid);
            return objectMapper.writeValueAsString(modifiedNode);
        } catch (Exception e) {
            log.warn("❌ Error reemplazando UID en JSON: {}", e.getMessage());
            return jsonNode.toString();
        }
    }

    private JsonNode replaceUidInJsonNode(JsonNode node, String uid) {
        if (node.isObject()) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode newNode = mapper.createObjectNode();

            node.fields().forEachRemaining(entry -> {
                String key = entry.getKey();
                JsonNode value = entry.getValue();

                if ("uid".equals(key) && value.isTextual()) {
                    ((com.fasterxml.jackson.databind.node.ObjectNode) newNode).put(key, uid);
                } else {
                    ((com.fasterxml.jackson.databind.node.ObjectNode) newNode).set(key,
                            replaceUidInJsonNode(value, uid));
                }
            });

            return newNode;
        } else if (node.isArray()) {
            com.fasterxml.jackson.databind.node.ArrayNode arrayNode = objectMapper.createArrayNode();
            node.elements().forEachRemaining(element -> arrayNode.add(replaceUidInJsonNode(element, uid)));
            return arrayNode;
        } else {
            return node;
        }
    }

    @Override
    public int getOrder() {
        // Ejecutar después de la autenticación (Ordered.LOWEST_PRECEDENCE ejecuta
        // último)
        return Ordered.LOWEST_PRECEDENCE;
    }
}