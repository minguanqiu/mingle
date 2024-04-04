package io.github.minguanq.mingle.svc.exception.handler.resolver;

import io.github.minguanq.mingle.svc.SvcResponse;
import io.github.minguanq.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;

/**
 * This class collect all register {@link AbstractExceptionHandler} and resolver exception
 *
 * @author Ming
 */

public class ExceptionHandlerResolver {

    private final HashMap<Class<?>, AbstractExceptionHandler<Exception>> exceptionHandlerMap = new HashMap<>();
    private final List<AbstractExceptionHandler<? extends Exception>> abstractExceptionHandlers;

    public ExceptionHandlerResolver(List<AbstractExceptionHandler<?>> abstractExceptionHandlers) {
        this.abstractExceptionHandlers = abstractExceptionHandlers;
    }

    public ResponseEntity<SvcResponse> resolver(Exception e) {
        return exceptionHandlerMap.getOrDefault(e.getClass(), exceptionHandlerMap.get(Exception.class)).handle(e);
    }

    @SuppressWarnings("unchecked")
    @PostConstruct
    private void init() {
        abstractExceptionHandlers.forEach(e -> {
            exceptionHandlerMap.put(e.getEClass(), (AbstractExceptionHandler<Exception>) e);
        });
    }


}
