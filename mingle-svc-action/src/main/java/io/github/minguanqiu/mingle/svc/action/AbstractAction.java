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
 * Base class for all action.
 *
 * @param <R1> action request.
 * @param <R2> action response body.
 * @author Qiu Guan Ming
 */
public abstract non-sealed class AbstractAction<R1 extends ActionRequest,
    R2 extends ActionResponseBody> implements Action<R1, R2> {

  /**
   * Action properties.
   */
  protected final ActionProperties actionProperties;
  private final List<ActionInterceptor> actionInterceptors = new ArrayList<>();
  private static final ActionResponseInterceptor actionResponseInterceptor =
      new ActionResponseInterceptor();
  private boolean setActionInterceptors;
  private boolean setActionExceptionHandlerResolver;
  private boolean setActionLoggingHandler;

  /**
   * Create a new AbstractAction implement instance.
   *
   * @param actionProperties the action properties.
   */
  public AbstractAction(ActionProperties actionProperties) {
    this.actionProperties = actionProperties;
    buildInterceptor();
  }

  @Override
  @SuppressWarnings("unchecked")
  public final ActionResponse<R2> doAction(R1 request) {
    ActionInfo actionInfo = buildActionInfo();
    ActionResponse<R2> actionResponse = buildActionResponse(actionInfo);
    ActionChain actionChain = new ActionChain(actionInterceptors, this, request, actionInfo, 0);
    actionChain.proceed();
    actionResponse.setCode(actionInfo.getCode());
    actionResponse.setMsg(actionInfo.getMsg());
    actionResponse.setResponseBody((R2) actionInfo.getActionResponseBody());
    checkSuccess(request.getAutoBreak(), actionResponse);
    return actionResponse;
  }

  /**
   * Build action information.
   *
   * @return return the action information.
   */
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

  /**
   * Build action response.
   *
   * @param actionInfo the action information.
   * @return return the action response.
   */
  private ActionResponse<R2> buildActionResponse(ActionInfo actionInfo) {
    ActionResponse<R2> actionResponse = new ActionResponse<>();
    actionResponse.setMsgType(actionProperties.getMsgType());
    actionResponse.setValues(actionInfo.getValues());
    return actionResponse;
  }

  /**
   * Process action logic.
   *
   * @param request    the action request.
   * @param actionInfo the action information.
   * @return return the action response body.
   */
  protected abstract R2 processLogic(R1 request, ActionInfo actionInfo);

  /**
   * Check action success or fail and auto break enable status.
   *
   * @param autoBreak      the automatic break.
   * @param actionResponse the action response.
   */
  protected final void checkSuccess(AutoBreak autoBreak,
      ActionResponse<R2> actionResponse) {
    if (!actionProperties.getCode().equals(actionResponse.getCode())) {
      switch (autoBreak) {
        case GLOBAL:
          if (actionProperties.isAutoBreak()) {
            throw new ActionAutoBreakException(actionResponse);
          }
          break;
        case TRUE:
          throw new ActionAutoBreakException(actionResponse);
        default:
      }
    } else {
      actionResponse.setSuccess(true);
    }
  }

  /**
   * Set action interceptors.
   *
   * @param actionInterceptors the list of action interceptors.
   */
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

  /**
   * Set action logging handler.
   *
   * @param actionLoggingHandler the action logging handler.
   */
  @Autowired(required = false)
  public void setActionLoggingHandler(
      ActionLoggingHandler actionLoggingHandler) {
    if (setActionLoggingHandler) {
      return;
    }
    setActionLoggingHandler = true;
    actionInterceptors.add(0, new ActionLoggingInterceptor(actionLoggingHandler));
  }

  /**
   * Set custom action exception resolver.
   *
   * @param actionExceptionHandlerResolver the action exception resolver.
   */
  @Autowired(required = false)
  public void setActionExceptionHandlerResolver(
      ActionExceptionHandlerResolver actionExceptionHandlerResolver) {
    if (setActionExceptionHandlerResolver) {
      return;
    }
    setActionExceptionHandlerResolver = true;
    actionInterceptors.add(1, new ActionExceptionInterceptor(actionExceptionHandlerResolver));
  }

  /**
   * Build default interceptor.
   */
  private void buildInterceptor() {
    this.actionInterceptors.add(actionResponseInterceptor);
  }


}
