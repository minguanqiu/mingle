package io.github.amings.mingle.svc.action.exception.handler;

import io.github.amings.mingle.svc.action.exception.handler.abs.AbstractActionExceptionHandler;
import io.github.amings.mingle.svc.action.exception.handler.model.ActionExceptionModel;

/**
 * @author Ming
 */

public class AllActionExceptionHandler extends AbstractActionExceptionHandler<Exception> {

    @Override
    public ActionExceptionModel handle(Exception ex, ActionExceptionModel actionExceptionModel) {
        actionExceptionModel.setCode("MGA01");
        actionExceptionModel.setDesc("exception message : " + ex.getMessage());
        return actionExceptionModel;
    }
}
