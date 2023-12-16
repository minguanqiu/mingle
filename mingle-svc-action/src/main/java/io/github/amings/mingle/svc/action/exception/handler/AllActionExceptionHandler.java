package io.github.amings.mingle.svc.action.exception.handler;

import io.github.amings.mingle.svc.action.ActionExceptionModel;
import io.github.amings.mingle.svc.action.annotation.ActionExceptionHandler;
import io.github.amings.mingle.svc.action.exception.handler.abs.AbstractActionExceptionHandler;

/**
 * @author Ming
 */

@ActionExceptionHandler
public class AllActionExceptionHandler extends AbstractActionExceptionHandler<Exception> {

    @Override
    public ActionExceptionModel handle(Exception ex, ActionExceptionModel actionExceptionModel) {
        actionExceptionModel.setCode("MGA01");
        actionExceptionModel.setDesc("exception message : " + ex.getMessage());
        return actionExceptionModel;
    }
}
