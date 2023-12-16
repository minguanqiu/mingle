package io.github.amings.mingle.svc.exception.handler;

import io.github.amings.mingle.svc.annotation.ExceptionHandler;
import io.github.amings.mingle.svc.config.properties.SvcProperties;
import io.github.amings.mingle.svc.exception.BreakFilterProcessException;
import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.filter.SvcInfo;
import io.github.amings.mingle.svc.handler.SvcMsgHandler;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import io.github.amings.mingle.svc.utils.SvcResUtils;
import org.springframework.http.ResponseEntity;

/**
 * when Svc Filter throw this Exception will be caught
 *
 * @author Ming
 */

@ExceptionHandler
public class BreakFilterProcessExceptionHandler extends AbstractExceptionHandler<BreakFilterProcessException> {

    public BreakFilterProcessExceptionHandler(SvcInfo svcInfo, SvcResUtils svcResUtils, SvcMsgHandler svcMsgHandler, SvcProperties svcProperties) {
        super(svcInfo, svcResUtils, svcMsgHandler, svcProperties);
    }

    @Override
    public ResponseEntity<SvcResModelHandler> handle(BreakFilterProcessException e) {
        return build(e.getCode(), e.getDesc());
    }

}
