package com.microservicios.marketplace_ms.exception;

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebInputException;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;

@Component
public class GraphQLExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof ServerWebInputException || ex instanceof HttpMessageNotReadableException) {
            return GraphQLError.newError()
                    .errorType(graphql.ErrorType.ValidationError)
                    .message("Error al procesar la solicitud: " + ex.getMessage())
                    .build();
        }
        return null; // Let other resolvers handle it
    }
}