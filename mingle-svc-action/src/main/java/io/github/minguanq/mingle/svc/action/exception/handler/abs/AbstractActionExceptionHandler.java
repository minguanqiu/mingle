package io.github.minguanq.mingle.svc.action.exception.handler.abs;

import com.google.common.reflect.TypeToken;
import io.github.minguanq.mingle.svc.action.exception.handler.model.ActionExceptionModel;
import lombok.Getter;

/**
 * Base class for action exception handler
 *
 * @author Ming
 */
@Getter
public abstract class AbstractActionExceptionHandler<E extends Exception> {

    private final Class<E> exceptionClass;

    @SuppressWarnings("unchecked")
    public AbstractActionExceptionHandler() {
        exceptionClass = (Class<E>) new TypeToken<E>(getClass()) {
        }.getRawType();
    }

    /**
     * Handle exception logic
     *
     * @param ex Exception
     */
    public abstract ActionExceptionModel handle(E ex, ActionExceptionModel actionExceptionModel);

}
