package io.github.amings.mingle.svc.exception.handler;

import io.github.amings.mingle.svc.SvcResponse;
import io.github.amings.mingle.svc.exception.BreakSvcProcessException;
import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.filter.SvcInfo;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Default handler will catch {@link BreakSvcProcessException}
 *
 * @author Ming
 */
@Component
@Order(Integer.MIN_VALUE)
public class BreakSvcProcessExceptionHandler extends AbstractExceptionHandler<BreakSvcProcessException> {

    public BreakSvcProcessExceptionHandler(SvcInfo svcInfo) {
        super(svcInfo);
    }

    @Override
    public ResponseEntity<SvcResponse> handle(BreakSvcProcessException e) {
        return build(e.getCode(), e.getMsg(), e.getSvcResponse());
    }

}
