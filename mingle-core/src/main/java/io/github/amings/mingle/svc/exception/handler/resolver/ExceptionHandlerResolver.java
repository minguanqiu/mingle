package io.github.amings.mingle.svc.exception.handler.resolver;

import io.github.amings.mingle.svc.annotation.ExceptionHandler;
import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
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
    @Autowired
    List<AbstractExceptionHandler<?>> abstractExceptionHandlers;

    public ResponseEntity<SvcResModelHandler> resolver(Exception e) {
        Exception exception = e;
        if (exception.getClass().equals(NestedServletException.class)) {
            exception = (Exception) e.getCause();
        }
        if (exceptionHandlerMap.containsKey(exception.getClass())) {
            return exceptionHandlerMap.get(exception.getClass()).handle(exception);
        }
        return exceptionHandlerMap.get(Exception.class).handle(exception);
    }

    @SuppressWarnings("unchecked")
    @PostConstruct
    private void init() {
        ArrayList<Class<?>> primaryClasses = new ArrayList<>();
        abstractExceptionHandlers.stream().filter(e -> e.getClass().getAnnotation(Deprecated.class) == null).filter(e -> e.getClass().getAnnotation(ExceptionHandler.class) != null).forEach(e -> {
            exceptionHandlerMap.put(e.getEClass(), (AbstractExceptionHandler<Exception>) e);
            if (e.getClass().getAnnotation(ExceptionHandler.class).primary()) {
                primaryClasses.add(e.getEClass());
            }
        });
        abstractExceptionHandlers.stream().filter(e -> e.getClass().getAnnotation(ExceptionHandler.class) == null).forEach(e -> {
            if (!primaryClasses.contains(e.getEClass())) {
                exceptionHandlerMap.put(e.getEClass(), (AbstractExceptionHandler<Exception>) e);
            }
        });
    }


}
