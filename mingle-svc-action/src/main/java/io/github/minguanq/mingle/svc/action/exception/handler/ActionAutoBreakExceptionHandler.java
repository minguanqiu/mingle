package io.github.minguanq.mingle.svc.action.exception.handler;

import io.github.minguanq.mingle.svc.SvcResponseBody;
import io.github.minguanq.mingle.svc.action.ActionResponse;
import io.github.minguanq.mingle.svc.action.exception.ActionAutoBreakException;
import io.github.minguanq.mingle.svc.exception.handler.AbstractExceptionHandler;
import io.github.minguanq.mingle.svc.filter.SvcInfo;
import io.github.minguanq.mingle.svc.handler.SvcMsgHandler;
import org.springframework.http.ResponseEntity;

/**
 * Default handler will catch {@link ActionAutoBreakException}
 *
 * @author Ming
 */

public class ActionAutoBreakExceptionHandler extends AbstractExceptionHandler<ActionAutoBreakException> {

    private final SvcMsgHandler svcMsgHandler;

    public ActionAutoBreakExceptionHandler(SvcInfo svcInfo, SvcMsgHandler svcMsgHandler) {
        super(svcInfo);
        this.svcMsgHandler = svcMsgHandler;
    }

    @Override
    public ResponseEntity<SvcResponseBody> handle(ActionAutoBreakException e) {
        ActionResponse<?> actionResponse = e.getActionResponse();
        return build(actionResponse.getCode(), svcMsgHandler.getMsg(actionResponse.getMsgType(), actionResponse.getCode()));
    }

}
