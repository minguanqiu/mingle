package io.github.minguanqiu.mingle.svc.action;

import io.github.minguanqiu.mingle.svc.action.exception.handler.model.ActionExceptionModel;
import io.github.minguanqiu.mingle.svc.action.exception.resolver.ActionExceptionHandlerResolver;

/**
 * Interceptor for execute action logic and process response
 *
 * @author Ming
 */
public class ActionResponseInterceptor implements ActionInterceptor {

    private final ActionExceptionHandlerResolver actionExceptionHandlerResolver;

    public ActionResponseInterceptor(ActionExceptionHandlerResolver actionExceptionHandlerResolver) {
        this.actionExceptionHandlerResolver = actionExceptionHandlerResolver;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void intercept(Chain chain) {
        AbstractAction action = chain.actionTarget();
        ActionResponse<ActionResponseBody> actionResponse = chain.response();
        ActionInfo actionInfo = new ActionInfo();
        try {
            actionInfo.setCode(chain.response().getCode());
            actionInfo.setMsg(chain.response().getMsg());
            actionInfo.setValues(actionResponse.getValues());
            ActionResponseBody responseBody = action.processLogic(chain.request(), actionInfo);
            actionResponse.setCode(actionInfo.getCode());
            actionResponse.setMsg(actionInfo.getMsg());
            actionResponse.setResponseBody(responseBody);
        } catch (Exception e) {
            if (actionExceptionHandlerResolver == null) {
                throw e;
            }
            ActionExceptionModel actionExceptionModel = actionExceptionHandlerResolver.resolver(e);
            actionResponse.setCode(actionExceptionModel.getCode());
            actionResponse.setMsg(actionExceptionModel.getMsg());
        }
        action.checkSuccess(chain.request().getAutoBreak(), actionResponse);
    }

}
