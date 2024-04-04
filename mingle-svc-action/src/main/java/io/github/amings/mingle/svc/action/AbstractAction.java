package io.github.amings.mingle.svc.action;

import io.github.amings.mingle.svc.action.concurrent.ActionAttribute;
import io.github.amings.mingle.svc.action.concurrent.ActionThreadLocal;
import io.github.amings.mingle.svc.action.configuration.properties.ActionProperties;
import io.github.amings.mingle.svc.action.enums.AutoBreak;
import io.github.amings.mingle.svc.action.exception.ActionAutoBreakException;
import io.github.amings.mingle.svc.action.exception.resolver.ActionExceptionHandlerResolver;

import java.util.ArrayList;
import java.util.List;


/**
 * Base class for all action
 *
 * <pre>
 * Action is a module,help uniform usage and logging
 *
 * Generic:
 * Req - action request
 * Res - action response body
 * </pre>
 *
 * @author Ming
 */
public abstract non-sealed class AbstractAction<Req extends ActionRequest, ResB extends ActionResponseBody> implements Action<Req, ResB> {

    protected final ActionProperties actionProperties;
    private List<ActionInterceptor> actionInterceptors;

    public AbstractAction(ActionProperties actionProperties, ActionExceptionHandlerResolver actionExceptionHandlerResolver, List<ActionInterceptor> actionInterceptors) {
        this.actionProperties = actionProperties;
        this.actionInterceptors = actionInterceptors;
        buildInterceptor(actionInterceptors, actionExceptionHandlerResolver);
    }

    /**
     * Execute Action logic
     *
     * @param reqModel Action request model
     * @return ResData
     */
    @SuppressWarnings("unchecked")
    public final ActionResponse<ResB> doAction(Req reqModel) {
        ActionResponse<ActionResponseBody> response = new ActionResponse<>();
        response.setCode(actionProperties.getSuccessCode());
        response.setMsg(actionProperties.getSuccessMsg());
        initActionAttributes();
        try {
            ActionChain actionChain = new ActionChain(actionInterceptors, this, reqModel, response, 0);
            actionChain.proceed();
            return (ActionResponse<ResB>) actionChain.response();
        } finally {
            ActionThreadLocal.remove();
        }
    }

    private void initActionAttributes() {
        ActionThreadLocal.set(new ActionAttribute());
    }


    protected abstract ActionInfo<ResB> processLogic(Req request, ActionInfo<ResB> actionInfo);

    /**
     * Defined action msg type
     *
     * @return String
     */
    public String getMsgType() {
        return actionProperties.getMsgType();
    }

    /**
     * Check action is success and set status
     *
     * @param autoBreak      autoBreak
     * @param actionResponse action response data
     */
    protected final void checkSuccess(AutoBreak autoBreak, ActionResponse<ResB> actionResponse) {
        if (!actionProperties.getSuccessCode().equals(actionResponse.getCode())) {
            switch (autoBreak) {
                case GLOBAL:
                    if (actionProperties.isAutoBreak()) {
                        throw new ActionAutoBreakException(actionResponse);
                    }
                    break;
                case TRUE:
                    throw new ActionAutoBreakException(actionResponse);
            }
        } else {
            actionResponse.setSuccess(true);
        }
    }

    private void buildInterceptor(List<ActionInterceptor> actionInterceptors, ActionExceptionHandlerResolver actionExceptionHandlerResolver) {
        this.actionInterceptors = new ArrayList<>(actionInterceptors);
//        this.actionInterceptors.add(0, new ActionProcessInterceptor());
        this.actionInterceptors.add(new ActionResponseInterceptor(actionExceptionHandlerResolver));
    }


}
