package io.github.amings.mingle.svc.exception.handler;

import io.github.amings.mingle.svc.SvcResponse;
import io.github.amings.mingle.svc.exception.BreakFilterProcessException;
import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.filter.SvcInfo;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Default handler will catch {@link BreakFilterProcessException}
 *
 * @author Ming
 */
@Component
@Order(Integer.MIN_VALUE)
public class BreakFilterProcessExceptionHandler extends AbstractExceptionHandler<BreakFilterProcessException> {

    public BreakFilterProcessExceptionHandler(SvcInfo svcInfo) {
        super(svcInfo);
    }

    @Override
    public ResponseEntity<SvcResponse> handle(BreakFilterProcessException e) {
        return build(e.getCode(), e.getMsg());
    }

}
