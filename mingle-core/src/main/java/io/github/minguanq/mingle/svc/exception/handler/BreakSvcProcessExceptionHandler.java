package io.github.minguanq.mingle.svc.exception.handler;

import io.github.minguanq.mingle.svc.SvcResponseBody;
import io.github.minguanq.mingle.svc.exception.BreakSvcProcessException;
import io.github.minguanq.mingle.svc.filter.SvcInfo;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;

/**
 * Default handler will catch {@link BreakSvcProcessException}
 *
 * @author Ming
 */
@Order(Integer.MIN_VALUE)
public class BreakSvcProcessExceptionHandler extends AbstractExceptionHandler<BreakSvcProcessException> {

    public BreakSvcProcessExceptionHandler(SvcInfo svcInfo) {
        super(svcInfo);
    }

    @Override
    public ResponseEntity<SvcResponseBody> handle(BreakSvcProcessException e) {
        return build(e.getSvcResponseHeader(), e.getSvcResponseBody());
    }

}
