package io.github.minguanq.mingle.svc.action.exception.handler;

import io.github.minguanq.mingle.svc.SvcResponseBody;
import io.github.minguanq.mingle.svc.SvcResponseHeader;
import io.github.minguanq.mingle.svc.action.ActionResponse;
import io.github.minguanq.mingle.svc.action.exception.ActionAutoBreakException;
import io.github.minguanq.mingle.svc.exception.handler.AbstractExceptionHandler;
import io.github.minguanq.mingle.svc.filter.SvcInfo;
import io.github.minguanq.mingle.svc.handler.CodeMessageHandler;
import org.springframework.http.ResponseEntity;

/**
 * Default handler will catch {@link ActionAutoBreakException}
 *
 * @author Ming
 */

public class ActionAutoBreakExceptionHandler extends AbstractExceptionHandler<ActionAutoBreakException> {

    private final CodeMessageHandler codeMessageHandler;

    public ActionAutoBreakExceptionHandler(SvcInfo svcInfo, CodeMessageHandler codeMessageHandler) {
        super(svcInfo);
        this.codeMessageHandler = codeMessageHandler;
    }

    @Override
    public ResponseEntity<SvcResponseBody> handle(ActionAutoBreakException e) {
        ActionResponse<?> actionResponse = e.getActionResponse();
        return build(SvcResponseHeader.builder(actionResponse.getCode()).msg(codeMessageHandler.getMsg(actionResponse.getMsgType(), actionResponse.getCode()).orElse(null)).build());
    }

}
