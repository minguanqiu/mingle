package io.github.minguanqiu.mingle.svc.action.exception.handler;

import io.github.minguanqiu.mingle.svc.SvcResponseBody;
import io.github.minguanqiu.mingle.svc.SvcResponseHeader;
import io.github.minguanqiu.mingle.svc.action.ActionResponse;
import io.github.minguanqiu.mingle.svc.action.exception.ActionAutoBreakException;
import io.github.minguanqiu.mingle.svc.exception.handler.AbstractExceptionHandler;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import io.github.minguanqiu.mingle.svc.handler.CodeMessageHandler;
import org.springframework.http.ResponseEntity;

/**
 * Default handler will catch {@link ActionAutoBreakException}.
 *
 * @author Qiu Guan Ming
 */

public class ActionAutoBreakExceptionHandler extends
    AbstractExceptionHandler<ActionAutoBreakException> {

  private final CodeMessageHandler codeMessageHandler;

  /**
   * Create a new ActionAutoBreakExceptionHandler instance.
   *
   * @param svcInfo            the service information.
   * @param codeMessageHandler the code message handler.
   */
  public ActionAutoBreakExceptionHandler(SvcInfo svcInfo, CodeMessageHandler codeMessageHandler) {
    super(svcInfo);
    this.codeMessageHandler = codeMessageHandler;
  }

  @Override
  public ResponseEntity<SvcResponseBody> handle(ActionAutoBreakException e) {
    ActionResponse<?> actionResponse = e.getActionResponse();
    String msg = e.getActionResponse().getMsg();
    if (msg == null) {
      msg = codeMessageHandler.getMsg(actionResponse.getMsgType(), actionResponse.getCode())
          .orElse(null);
    }
    return build(SvcResponseHeader.builder(actionResponse.getCode()).msg(msg).build());
  }

}
