package io.github.amings.mingle.svc.action.interceptor;

import io.github.amings.mingle.svc.action.*;

/**
 * @author Ming
 */

public interface ActionInterceptor {

    boolean supported(AbstractAction<? extends ActionReqModel, ? extends ActionResData, ? extends ActionResModel> action);

    void before(AbstractAction<? extends ActionReqModel, ? extends ActionResData, ? extends ActionResModel> action, ActionReqModel actionReqModel);

    void after(AbstractAction<? extends ActionReqModel, ? extends ActionResData, ? extends ActionResModel> action, ActionResponse<? extends ActionResData, ? extends ActionResModel> actionResponse);

}
