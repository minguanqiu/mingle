package io.github.amings.mingle.svc.action.interceptor.impl;

import io.github.amings.mingle.svc.action.*;
import io.github.amings.mingle.svc.action.handler.ActionLogHandler;
import io.github.amings.mingle.svc.action.handler.model.ActionBeginModel;
import io.github.amings.mingle.svc.action.handler.model.ActionEndModel;
import io.github.amings.mingle.svc.action.interceptor.ActionInterceptor;

import java.time.LocalDateTime;

public class LoggingInterceptorDefaultImpl implements ActionInterceptor {

    private final ActionLogHandler actionLogHandler;

    public LoggingInterceptorDefaultImpl(ActionLogHandler actionLogHandler) {
        this.actionLogHandler = actionLogHandler;
    }

    @Override
    public boolean supported(AbstractAction<? extends ActionReqModel, ? extends ActionResData, ? extends ActionResModel> action) {
        return true;
    }

    @Override
    public void before(AbstractAction<? extends ActionReqModel, ? extends ActionResData, ? extends ActionResModel> action, ActionReqModel actionReqModel) {
//        ActionLogThreadLocal.set();
        ActionBeginModel actionBeginModel = new ActionBeginModel();
        actionBeginModel.setStartDateTime(LocalDateTime.now());
        actionLogHandler.writeBeginLog(action, actionReqModel);
    }

    @Override
    public void after(AbstractAction<? extends ActionReqModel, ? extends ActionResData, ? extends ActionResModel> action, ActionResponse<? extends ActionResData, ? extends ActionResModel> actionResponse) {
        ActionEndModel actionEndModel = new ActionEndModel();
        actionEndModel.setEndDateTime(LocalDateTime.now());
        actionLogHandler.writeEndLog(action, actionResponse);
    }

}
