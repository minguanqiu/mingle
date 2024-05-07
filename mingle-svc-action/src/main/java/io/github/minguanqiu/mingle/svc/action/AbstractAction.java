package io.github.minguanqiu.mingle.svc.action;

import io.github.minguanqiu.mingle.svc.action.concurrent.ActionAttribute;
import io.github.minguanqiu.mingle.svc.action.concurrent.ActionThreadLocal;
import io.github.minguanqiu.mingle.svc.action.configuration.properties.SvcActionProperties;
import io.github.minguanqiu.mingle.svc.action.enums.AutoBreak;
import io.github.minguanqiu.mingle.svc.action.exception.ActionAutoBreakException;
import io.github.minguanqiu.mingle.svc.action.exception.resolver.ActionExceptionHandlerResolver;

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

    protected final SvcActionProperties svcActionProperties;
    private List<ActionInterceptor> actionInterceptors;

    public AbstractAction(SvcActionProperties svcActionProperties, ActionExceptionHandlerResolver actionExceptionHandlerResolver, List<ActionInterceptor> actionInterceptors) {
        this.svcActionProperties = svcActionProperties;
        this.actionInterceptors = actionInterceptors;
        buildInterceptor(actionInterceptors, actionExceptionHandlerResolver);
    }

    /**
     * Execute Action logic
     *
     * @param request Action request model
     * @return ResData
     */
    @SuppressWarnings("unchecked")
    public final ActionResponse<ResB> doAction(Req request) {
        ActionResponse<ActionResponseBody> response = new ActionResponse<>();
        response.setMsgType(svcActionProperties.getMsg_type());
        response.setCode(svcActionProperties.getCode());
        response.setMsg(svcActionProperties.getMsg());
        initActionAttributes();
        try {
            ActionChain actionChain = new ActionChain(actionInterceptors, this, request, response, 0);
            actionChain.proceed();
            return (ActionResponse<ResB>) actionChain.response();
        } finally {
            ActionThreadLocal.remove();
        }
    }

    private void initActionAttributes() {
        ActionThreadLocal.set(new ActionAttribute());
    }


    protected abstract ResB processLogic(Req request, ActionInfo actionInfo);

    /**
     * Check action is success and set status
     *
     * @param autoBreak      autoBreak
     * @param actionResponse action response data
     */
    protected final void checkSuccess(AutoBreak autoBreak, ActionResponse<ResB> actionResponse) {
        if (!svcActionProperties.getCode().equals(actionResponse.getCode())) {
            switch (autoBreak) {
                case GLOBAL:
                    if (svcActionProperties.isAuto_break()) {
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
