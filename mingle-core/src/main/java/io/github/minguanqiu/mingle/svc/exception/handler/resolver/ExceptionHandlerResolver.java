package io.github.minguanqiu.mingle.svc.exception.handler.resolver;

import io.github.minguanqiu.mingle.svc.SvcResponseBody;
import io.github.minguanqiu.mingle.svc.exception.handler.AbstractExceptionHandler;
import java.util.HashMap;
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 * This class collect and register all {@link AbstractExceptionHandler} and resolve exception.
 *
 * @author Qiu Guan Ming
 */

public class ExceptionHandlerResolver {

  private final HashMap<Class<?>, AbstractExceptionHandler<Exception>> exceptionHandlerMap = new HashMap<>();
  private final List<AbstractExceptionHandler<? extends Exception>> abstractExceptionHandlers;

  public ExceptionHandlerResolver(List<AbstractExceptionHandler<?>> abstractExceptionHandlers) {
    this.abstractExceptionHandlers = abstractExceptionHandlers;
    init();
  }

  public ResponseEntity<SvcResponseBody> resolver(Exception e) {
    return exceptionHandlerMap.getOrDefault(e.getClass(), exceptionHandlerMap.get(Exception.class))
        .handle(e);
  }

  @SuppressWarnings("unchecked")
  private void init() {
    abstractExceptionHandlers.forEach(e -> {
      exceptionHandlerMap.put(e.getEClass(), (AbstractExceptionHandler<Exception>) e);
    });
  }


}
