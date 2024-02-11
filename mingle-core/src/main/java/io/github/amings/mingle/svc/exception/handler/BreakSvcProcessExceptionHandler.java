package io.github.amings.mingle.svc.exception.handler;

import io.github.amings.mingle.svc.AbstractSvcLogic;
import io.github.amings.mingle.svc.annotation.ExceptionHandler;
import io.github.amings.mingle.svc.configuration.properties.SvcProperties;
import io.github.amings.mingle.svc.exception.BreakSvcProcessException;
import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.filter.SvcInfo;
import io.github.amings.mingle.svc.handler.SvcMsgHandler;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import io.github.amings.mingle.svc.utils.SvcResUtils;
import org.springframework.http.ResponseEntity;

/**
 * Break Svc Process Exception Handler
 *
 * @author Ming
 * @see AbstractSvcLogic
 */

@ExceptionHandler
public class BreakSvcProcessExceptionHandler extends AbstractExceptionHandler<BreakSvcProcessException> {

    public BreakSvcProcessExceptionHandler(SvcInfo svcInfo, SvcResUtils svcResUtils, SvcMsgHandler svcMsgHandler, SvcProperties svcProperties) {
        super(svcInfo, svcResUtils, svcMsgHandler, svcProperties);
    }

    @Override
    public ResponseEntity<SvcResModelHandler> handle(BreakSvcProcessException e) {
        return build(e.getCode(), e.getDesc(), e.getSvcResModel());
    }

}
