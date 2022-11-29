package io.github.amings.mingle.svc.exception.handler;

import io.github.amings.mingle.svc.action.ActionResData;
import io.github.amings.mingle.svc.action.ActionResModel;
import io.github.amings.mingle.svc.annotation.ExceptionHandler;
import io.github.amings.mingle.svc.exception.ActionAutoBreakException;
import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import org.springframework.http.ResponseEntity;

/**
 * ActionAutoBreakException handler
 *
 * @author Ming
 */

@ExceptionHandler(primary = true)
public class ActionAutoBreakExceptionHandler extends AbstractExceptionHandler<ActionAutoBreakException> {

    @Override
    public ResponseEntity<SvcResModelHandler> handle(ActionAutoBreakException e) {
        ActionResData<? extends ActionResModel> actionResData = e.getActionResData();
        return build(actionResData.getCode(), svcMsgHandler.getMsg(actionResData));
    }

}
