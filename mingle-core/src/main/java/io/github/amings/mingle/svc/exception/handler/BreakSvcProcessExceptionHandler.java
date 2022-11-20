package io.github.amings.mingle.svc.exception.handler;

import io.github.amings.mingle.svc.AbstractSvcLogic;
import io.github.amings.mingle.svc.exception.BreakSvcProcessException;
import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Break Svc Process Exception Handler
 *
 * @author Ming
 * @see AbstractSvcLogic
 */
@RestControllerAdvice
public class BreakSvcProcessExceptionHandler extends AbstractExceptionHandler<BreakSvcProcessException> {

    @ExceptionHandler(BreakSvcProcessException.class)
    @Override
    public ResponseEntity<SvcResModelHandler> handle(BreakSvcProcessException e) {
        return build(e.getCode(), e.getDesc(), e.getSvcResModel());
    }

}
