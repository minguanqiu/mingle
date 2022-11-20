package io.github.amings.mingle.svc.exception.handler.resolver;

import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import io.github.amings.mingle.utils.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
    List<AbstractExceptionHandler<?>> abstractExceptionHandlerList;

    public ResponseEntity<SvcResModelHandler> resolver(Exception e) {
        if (exceptionHandlerMap.containsKey(e.getClass())) {
            return exceptionHandlerMap.get(e.getClass()).handle(e);
        }
        return exceptionHandlerMap.get(Exception.class).handle(e);
    }

    @PostConstruct
    private void init() {
        abstractExceptionHandlerList.forEach(e -> {
            Class<?> genericClass = ReflectionUtils.getGenericClass(e.getClass(), 0);
            exceptionHandlerMap.put(genericClass, (AbstractExceptionHandler) e);
        });
    }


}
