package io.github.minguanq.mingle.svc.action;

import java.util.List;

/**
 * Interceptor chain for action
 *
 * @author Ming
 */
public class ActionChain implements ActionInterceptor.Chain {

    private final List<ActionInterceptor> actionInterceptors;

    private final AbstractAction<? extends ActionRequest, ? extends ActionResponseBody> action;
    private final ActionRequest actionRequest;
    private final ActionResponse<ActionResponseBody> actionResponse;
    private final int index;
    private int callTimes;


    public ActionChain(List<ActionInterceptor> actionInterceptors, AbstractAction<? extends ActionRequest, ? extends ActionResponseBody> action, ActionRequest actionRequest, ActionResponse<ActionResponseBody> actionResponse, int index) {
        this.actionInterceptors = actionInterceptors;
        this.action = action;
        this.actionRequest = actionRequest;
        this.actionResponse = actionResponse;
        this.index = index;
    }

    @Override
    public void proceed() {
        if (index > actionInterceptors.size()) throw new IllegalStateException("");
        callTimes++;
        if (callTimes == 1) { // prevention chain call twice
            actionInterceptors.get(index).intercept(new ActionChain(actionInterceptors, action, actionRequest, actionResponse, index + 1));
        } else {
            throw new IllegalStateException("action interceptor " + this.actionInterceptors.get(index).getClass().getSimpleName() + " proceed() must call once");
        }
    }

    @Override
    public AbstractAction<? extends ActionRequest, ? extends ActionResponseBody> actionTarget() {
        return action;
    }

    @Override
    public ActionRequest request() {
        return actionRequest;
    }

    @Override
    public ActionResponse<ActionResponseBody> response() {
        return actionResponse;
    }

}
