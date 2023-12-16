package io.github.amings.mingle.svc.exception.handler.resolver;

import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import io.github.amings.mingle.utils.ReflectionUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;

/**
 * Exception resolver
 *
 * @author Ming
 */

@Component
public class ExceptionHandlerResolver {

    private final HashMap<Class<?>, AbstractExceptionHandler<Exception>> exceptionHandlerMap = new HashMap<>();
    List<AbstractExceptionHandler<? extends Exception>> abstractExceptionHandlers;

    public ExceptionHandlerResolver(List<AbstractExceptionHandler<?>> abstractExceptionHandlers) {
        this.abstractExceptionHandlers = abstractExceptionHandlers;
    }

    public ResponseEntity<SvcResModelHandler> resolver(Exception e) {
        return exceptionHandlerMap.getOrDefault(e.getClass(), exceptionHandlerMap.get(Exception.class)).handle(e);
    }

    @SuppressWarnings("unchecked")
    @PostConstruct
    private void init() {
        abstractExceptionHandlers.forEach(e -> {
            ParameterizedType genericSuperclass = (ParameterizedType) ReflectionUtils.findGenericSuperclass(e.getClass(), AbstractExceptionHandler.class);
            exceptionHandlerMap.put((Class<?>) genericSuperclass.getActualTypeArguments()[0], (AbstractExceptionHandler<Exception>) e);
        });
    }


}
