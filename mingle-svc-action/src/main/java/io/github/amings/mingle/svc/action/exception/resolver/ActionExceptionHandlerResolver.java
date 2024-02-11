package io.github.amings.mingle.svc.action.exception.resolver;

import io.github.amings.mingle.svc.action.exception.handler.abs.AbstractActionExceptionHandler;
import io.github.amings.mingle.svc.action.exception.handler.model.ActionExceptionModel;

import java.util.HashMap;
import java.util.List;

/**
 * Exception resolver
 *
 * @author Ming
 */

public class ActionExceptionHandlerResolver {

    private final HashMap<Class<?>, AbstractActionExceptionHandler<Exception>> exceptionHandlerMap = new HashMap<>();
    private final List<AbstractActionExceptionHandler<?>> abstractExceptionHandlers;

    public ActionExceptionHandlerResolver(List<AbstractActionExceptionHandler<?>> abstractExceptionHandlers) {
        this.abstractExceptionHandlers = abstractExceptionHandlers;
        init();
    }

    public ActionExceptionModel resolver(Exception e) {
        if (exceptionHandlerMap.containsKey(e.getClass())) {
            return exceptionHandlerMap.get(e.getClass()).handle(e, new ActionExceptionModel());
        }
        return exceptionHandlerMap.get(Exception.class).handle(e, new ActionExceptionModel());
    }

    @SuppressWarnings("unchecked")
    private void init() {
        abstractExceptionHandlers.forEach(e -> exceptionHandlerMap.put(e.getExceptionClass(), (AbstractActionExceptionHandler<Exception>) e));
    }


}
