package io.github.minguanqiu.mingle.svc.action;

import io.github.minguanqiu.mingle.svc.action.configuration.properties.ActionProperties;
import io.github.minguanqiu.mingle.svc.action.enums.AutoBreak;
import io.github.minguanqiu.mingle.svc.action.exception.ActionAutoBreakException;
import io.github.minguanqiu.mingle.svc.action.exception.resolver.ActionExceptionHandlerResolver;
import io.github.minguanqiu.mingle.svc.action.handler.ActionLoggingHandler;
import io.github.minguanqiu.mingle.svc.concurrent.SvcAttributeName;
import io.github.minguanqiu.mingle.svc.concurrent.SvcThreadLocal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;


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
 * @author Qiu Guan Ming
 */
public abstract non-sealed class AbstractAction<Req extends ActionRequest,
    ResB extends ActionResponseBody> implements Action<Req, ResB> {

  protected final ActionProperties actionProperties;
  private final List<ActionInterceptor> actionInterceptors = new ArrayList<>();
  private static final ActionResponseInterceptor actionResponseInterceptor =
      new ActionResponseInterceptor();
  private boolean setActionInterceptors;
  private boolean setActionExceptionHandlerResolver;
  private boolean setActionLoggingHandler;

  public AbstractAction(ActionProperties actionProperties) {
    this.actionProperties = actionProperties;
    buildInterceptor();
  }

  /**
   * Execute Action logic
   *
   * @param request Action request model
   * @return ResData
   */
  @SuppressWarnings("unchecked")
  public final ActionResponse<ResB> doAction(Req request) {
    ActionInfo actionInfo = buildActionInfo();
    ActionResponse<ResB> actionResponse = buildActionResponse(actionInfo);
    ActionChain actionChain = new ActionChain(actionInterceptors, this, request, actionInfo, 0);
    actionChain.proceed();
    actionResponse.setCode(actionInfo.getCode());
    actionResponse.setMsg(actionInfo.getMsg());
    actionResponse.setResponseBody((ResB) actionInfo.getActionResponseBody());
    checkSuccess(request.getAutoBreak(), actionResponse);
    return actionResponse;
  }

  private ActionInfo buildActionInfo() {
    ActionInfo actionInfo = new ActionInfo();
    actionInfo.setActionClass(this.getClass());
    SvcThreadLocal.get()
        .flatMap(attribute -> attribute.getAttributes(SvcAttributeName.SVC_SERIAL_NUMBER))
        .ifPresent(serialNumber -> actionInfo.setSvcSerialNum((String) serialNumber));
    actionInfo.setActSerialNum(UUID.randomUUID().toString());
    actionInfo.setCode(actionProperties.getCode());
    actionInfo.setMsg(actionProperties.getMsg());
    return actionInfo;
  }

  private ActionResponse<ResB> buildActionResponse(ActionInfo actionInfo) {
    ActionResponse<ResB> actionResponse = new ActionResponse<>();
    actionResponse.setMsgType(actionProperties.getMsgType());
    actionResponse.setValues(actionInfo.getValues());
    return actionResponse;
  }

  protected abstract ResB processLogic(Req request, ActionInfo actionInfo);

  /**
   * Check action is success and set status
   *
   * @param autoBreak      autoBreak
   * @param actionResponse action response data
   */
  protected final void checkSuccess(AutoBreak autoBreak,
      ActionResponse<ResB> actionResponse) {
    if (!actionProperties.getCode().equals(actionResponse.getCode())) {
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

  @Autowired(required = false)
  public void setActionInterceptors(List<ActionInterceptor> actionInterceptors) {
    if (setActionInterceptors) {
      return;
    }
    setActionInterceptors = true;
    int count = 0;
    if (setActionLoggingHandler) {
      count++;
    }
    if (setActionExceptionHandlerResolver) {
      count++;
    }
    for (ActionInterceptor actionInterceptor : actionInterceptors) {
      this.actionInterceptors.add(count, actionInterceptor);
      count++;
    }
  }

  @Autowired(required = false)
  public void setActionLoggingHandler(
      ActionLoggingHandler actionLoggingHandler) {
    if (setActionLoggingHandler) {
      return;
    }
    setActionLoggingHandler = true;
    actionInterceptors.add(0, new ActionLoggingInterceptor(actionLoggingHandler));
  }

  @Autowired(required = false)
  public void setActionExceptionHandlerResolver(
      ActionExceptionHandlerResolver actionExceptionHandlerResolver) {
    if (setActionExceptionHandlerResolver) {
      return;
    }
    setActionExceptionHandlerResolver = true;
    actionInterceptors.add(1, new ActionExceptionInterceptor(actionExceptionHandlerResolver));
  }

  private void buildInterceptor() {
    this.actionInterceptors.add(actionResponseInterceptor);
  }


}
