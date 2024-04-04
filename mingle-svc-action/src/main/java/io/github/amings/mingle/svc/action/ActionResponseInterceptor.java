package io.github.amings.mingle.svc.action;

import io.github.amings.mingle.svc.action.concurrent.ActionAttribute;
import io.github.amings.mingle.svc.action.concurrent.ActionThreadLocal;
import io.github.amings.mingle.svc.action.exception.handler.model.ActionExceptionModel;
import io.github.amings.mingle.svc.action.exception.resolver.ActionExceptionHandlerResolver;

import java.util.Optional;

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
        ActionInfo actionInfo = new ActionInfo<>();
        try {
            actionInfo.setCode(chain.response().getCode());
            actionInfo.setMsg(chain.response().getMsg());
            actionInfo = action.processLogic(chain.request(), actionInfo);
            actionResponse.setCode(actionInfo.getCode());
            actionResponse.setMsg(actionInfo.getMsg());
            actionResponse.setValues(actionInfo.getValues());
            actionResponse.setResponseBody(actionInfo.getResponseBody());
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

    private void checkProcessStatus() {
        try {
            ActionAttribute actionAttribute = ActionThreadLocal.get();
            Optional<Object> optionalObject = actionAttribute.getAttributes(ActionAttribute.Name.ACTION_PROCESS_STATUS);
            ActionProcessInterceptor.ActionProcessStatus actionProcessStatus = (ActionProcessInterceptor.ActionProcessStatus) optionalObject.get();
            actionProcessStatus.setIntercepting(false);
        } catch (Exception e) {
            throw new IllegalStateException("action process status error");
        }

    }

}
