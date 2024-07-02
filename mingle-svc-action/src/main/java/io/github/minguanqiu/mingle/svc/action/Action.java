package io.github.minguanqiu.mingle.svc.action;

/**
 * <pre>
 * Base class for all action
 *
 * Generic:
 * Req - action request
 * Res - action response body
 * </pre>
 *
 * @author Qiu Guan Ming
 */
public sealed interface Action<Req extends ActionRequest, ResB extends ActionResponseBody> permits
    AbstractAction {

  ActionResponse<ResB> doAction(Req reqModel);

  String getType();

}
