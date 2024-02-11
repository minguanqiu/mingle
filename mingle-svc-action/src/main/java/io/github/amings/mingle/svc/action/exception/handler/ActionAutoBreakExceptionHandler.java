package io.github.amings.mingle.svc.action.exception.handler;

import io.github.amings.mingle.svc.action.ActionResponse;
import io.github.amings.mingle.svc.action.exception.ActionAutoBreakException;
import io.github.amings.mingle.svc.config.properties.SvcProperties;
import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.filter.SvcInfo;
import io.github.amings.mingle.svc.handler.SvcMsgHandler;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import io.github.amings.mingle.svc.utils.SvcResUtils;
import org.springframework.http.ResponseEntity;

/**
 * ActionAutoBreakException handler
 *
 * @author Ming
 */

public class ActionAutoBreakExceptionHandler extends AbstractExceptionHandler<ActionAutoBreakException> {

    public ActionAutoBreakExceptionHandler(SvcInfo svcInfo, SvcResUtils svcResUtils, SvcMsgHandler svcMsgHandler, SvcProperties svcProperties) {
        super(svcInfo, svcResUtils, svcMsgHandler, svcProperties);
    }

    @Override
    public ResponseEntity<SvcResModelHandler> handle(ActionAutoBreakException e) {
        ActionResponse<?, ?> actionResponse = e.getActionResponse();
        return build(actionResponse.getCode(), svcMsgHandler.getMsg(actionResponse.getMsgType(), actionResponse.getCode()));
    }

}
