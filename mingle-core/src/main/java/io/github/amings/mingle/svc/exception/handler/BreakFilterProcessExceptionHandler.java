package io.github.amings.mingle.svc.exception.handler;

import io.github.amings.mingle.svc.annotation.ExceptionHandler;
import io.github.amings.mingle.svc.exception.BreakFilterProcessException;
import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import org.springframework.http.ResponseEntity;

/**
 * when Svc Filter throw this Exception will be caught
 *
 * @author Ming
 */

@ExceptionHandler(primary = true)
public class BreakFilterProcessExceptionHandler extends AbstractExceptionHandler<BreakFilterProcessException> {

    @Override
    public ResponseEntity<SvcResModelHandler> handle(BreakFilterProcessException e) {
        return build(e.getCode(),e.getDesc());
    }

}
