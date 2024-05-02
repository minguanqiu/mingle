package io.github.minguanq.mingle.svc.action.exception.handler;

import io.github.minguanq.mingle.svc.action.exception.handler.abs.AbstractActionExceptionHandler;
import io.github.minguanq.mingle.svc.action.exception.handler.model.ActionExceptionModel;

/**
 * Default handler will catch {@link Exception} or unknown exception
 *
 * @author Ming
 */

public class AllActionExceptionHandler extends AbstractActionExceptionHandler<Exception> {

    @Override
    public ActionExceptionModel handle(Exception ex, ActionExceptionModel actionExceptionModel) {
        actionExceptionModel.setCode("error");
        actionExceptionModel.setMsg(ex.getMessage());
        return actionExceptionModel;
    }
}
