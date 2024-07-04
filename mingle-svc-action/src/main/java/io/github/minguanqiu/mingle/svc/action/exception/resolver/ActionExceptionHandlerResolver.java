package io.github.minguanqiu.mingle.svc.action.exception.resolver;

import io.github.minguanqiu.mingle.svc.action.exception.handler.abs.AbstractActionExceptionHandler;
import io.github.minguanqiu.mingle.svc.action.exception.handler.model.ActionExceptionModel;
import java.util.HashMap;
import java.util.List;

/**
 * This class provide register all {@link AbstractActionExceptionHandler} and resolver custom
 * exception.
 *
 * @author Qiu Guan Ming
 */

public class ActionExceptionHandlerResolver {

  private final HashMap<Class<?>, AbstractActionExceptionHandler<Exception>> exceptionHandlerMap = new HashMap<>();
  private final List<AbstractActionExceptionHandler<?>> abstractExceptionHandlers;

  /**
   * Create a new ActionExceptionHandlerResolver instance.
   *
   * @param abstractExceptionHandlers the list of exception handlers.
   */
  public ActionExceptionHandlerResolver(
      List<AbstractActionExceptionHandler<?>> abstractExceptionHandlers) {
    this.abstractExceptionHandlers = abstractExceptionHandlers;
    init();
  }

  /**
   * Resolve exception and find handler.
   *
   * @param e the exception.
   * @return return the action exception model.
   */
  public ActionExceptionModel resolver(Exception e) {
    if (exceptionHandlerMap.containsKey(e.getClass())) {
      return exceptionHandlerMap.get(e.getClass()).handle(e, new ActionExceptionModel());
    }
    return exceptionHandlerMap.get(Exception.class).handle(e, new ActionExceptionModel());
  }

  /**
   * Initialized when the object is created
   */
  @SuppressWarnings("unchecked")
  private void init() {
    abstractExceptionHandlers.forEach(e -> exceptionHandlerMap.put(e.getExceptionClass(),
        (AbstractActionExceptionHandler<Exception>) e));
  }


}
