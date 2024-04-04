package io.github.amings.mingle.svc.action.exception.handler;

import io.github.amings.mingle.svc.SvcResponse;
import io.github.amings.mingle.svc.action.ActionResponse;
import io.github.amings.mingle.svc.action.exception.ActionAutoBreakException;
import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.filter.SvcInfo;
import io.github.amings.mingle.svc.handler.SvcMsgHandler;
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
    public ResponseEntity<SvcResponse> handle(ActionAutoBreakException e) {
        ActionResponse<?> actionResponse = e.getActionResponse();
        return build(actionResponse.getCode(), svcMsgHandler.getMsg(actionResponse.getMsgType(), actionResponse.getCode()));
    }

}
