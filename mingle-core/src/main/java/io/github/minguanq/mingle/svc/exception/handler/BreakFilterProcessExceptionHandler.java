package io.github.minguanq.mingle.svc.exception.handler;

import io.github.minguanq.mingle.svc.SvcResponseBody;
import io.github.minguanq.mingle.svc.SvcResponseHeader;
import io.github.minguanq.mingle.svc.exception.BreakFilterProcessException;
import io.github.minguanq.mingle.svc.filter.SvcInfo;
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
    public ResponseEntity<SvcResponseBody> handle(BreakFilterProcessException e) {
        return build(SvcResponseHeader.builder(e.getCode()).msg(e.getMsg()).build());
    }

}
