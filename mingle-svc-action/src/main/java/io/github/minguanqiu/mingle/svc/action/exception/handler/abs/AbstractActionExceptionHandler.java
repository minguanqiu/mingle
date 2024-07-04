package io.github.minguanqiu.mingle.svc.action.exception.handler.abs;

import com.google.common.reflect.TypeToken;
import io.github.minguanqiu.mingle.svc.action.exception.handler.model.ActionExceptionModel;
import lombok.Getter;

/**
 * Base class for action exception handler.
 *
 * @author Qiu Guan Ming
 */
@Getter
public abstract class AbstractActionExceptionHandler<E extends Exception> {

  /**
   * Exception class
   *
   * @return return the exception class.
   */
  private final Class<E> exceptionClass;

  /**
   * Create a new AbstractActionExceptionHandler implement instance.
   */
  @SuppressWarnings("unchecked")
  public AbstractActionExceptionHandler() {
    exceptionClass = (Class<E>) new TypeToken<E>(getClass()) {
    }.getRawType();
  }

  /**
   * Handle exception logic.
   *
   * @param ex                   the exception.
   * @param actionExceptionModel the action exception model.
   * @return return the action exception model.
   */
  public abstract ActionExceptionModel handle(E ex, ActionExceptionModel actionExceptionModel);

}
