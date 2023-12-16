package io.github.amings.mingle.svc.action.exception.resolver;

import io.github.amings.mingle.svc.action.ActionExceptionModel;
import io.github.amings.mingle.svc.action.annotation.ActionExceptionHandler;
import io.github.amings.mingle.svc.action.exception.handler.abs.AbstractActionExceptionHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Exception resolver
 *
 * @author Ming
 */

@Component
public class ActionExceptionHandlerResolver {

    private final HashMap<Class<?>, AbstractActionExceptionHandler<Exception>> exceptionHandlerMap = new HashMap<>();
    @Autowired
    List<AbstractActionExceptionHandler<?>> abstractExceptionHandlers;

    public ActionExceptionModel resolver(Exception e) {
        if (exceptionHandlerMap.containsKey(e.getClass())) {
            return exceptionHandlerMap.get(e.getClass()).handle(e, new ActionExceptionModel());
        }
        return exceptionHandlerMap.get(Exception.class).handle(e, new ActionExceptionModel());
    }

    @SuppressWarnings("unchecked")
    @PostConstruct
    private void init() {
        ArrayList<Class<?>> primaryClasses = new ArrayList<>();
        abstractExceptionHandlers.stream().filter(e -> e.getClass().getAnnotation(ActionExceptionHandler.class) != null).forEach(e -> {
            exceptionHandlerMap.put(e.getEClass(), (AbstractActionExceptionHandler<Exception>) e);
            if (e.getClass().getAnnotation(ActionExceptionHandler.class).primary()) {
                primaryClasses.add(e.getEClass());
            }
        });
        abstractExceptionHandlers.stream().filter(e -> e.getClass().getAnnotation(ActionExceptionHandler.class) == null).forEach(e -> {
            if (!primaryClasses.contains(e.getEClass())) {
                exceptionHandlerMap.put(e.getEClass(), (AbstractActionExceptionHandler<Exception>) e);
            }
        });
    }


}
