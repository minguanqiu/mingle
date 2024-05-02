package io.github.minguanq.mingle.svc.action;

/**
 * Interface for action interceptor
 *
 * @author Ming
 */
public interface ActionInterceptor {

    void intercept(Chain chain);

    /**
     * Interface for action chain
     *
     * @author Ming
     */
    interface Chain {

        void proceed();

        AbstractAction<? extends ActionRequest, ? extends ActionResponseBody> actionTarget();

        ActionRequest request();

        ActionResponse<ActionResponseBody> response();

    }

}
