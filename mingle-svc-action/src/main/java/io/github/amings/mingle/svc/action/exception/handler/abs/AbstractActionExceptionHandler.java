package io.github.amings.mingle.svc.action.exception.handler.abs;

import com.google.common.reflect.TypeToken;
import io.github.amings.mingle.svc.action.ActionExceptionModel;
import lombok.Getter;

/**
 * @author Ming
 */
public abstract class AbstractActionExceptionHandler<E extends Exception> {

    @Getter
    private final Class<E> eClass;

    @SuppressWarnings("unchecked")
    public AbstractActionExceptionHandler() {
        eClass = (Class<E>) new TypeToken<E>(getClass()) {
        }.getRawType();
    }

    /**
     * Handle exception logic
     *
     * @param ex Exception
     */
    public abstract ActionExceptionModel handle(E ex, ActionExceptionModel actionExceptionModel);

}
