package io.github.amings.mingle.svc.exception.handler;

import io.github.amings.mingle.svc.AbstractSvcLogic;
import io.github.amings.mingle.svc.annotation.ExceptionHandler;
import io.github.amings.mingle.svc.exception.BreakSvcProcessException;
import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import org.springframework.http.ResponseEntity;

/**
 * Break Svc Process Exception Handler
 *
 * @author Ming
 * @see AbstractSvcLogic
 */

@ExceptionHandler(primary = true)
public class BreakSvcProcessExceptionHandler extends AbstractExceptionHandler<BreakSvcProcessException> {

    @Override
    public ResponseEntity<SvcResModelHandler> handle(BreakSvcProcessException e) {
        return build(e.getCode(), e.getDesc(), e.getSvcResModel());
    }

}
