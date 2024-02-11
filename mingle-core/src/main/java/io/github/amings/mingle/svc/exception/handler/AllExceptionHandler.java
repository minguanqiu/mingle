package io.github.amings.mingle.svc.exception.handler;

import io.github.amings.mingle.svc.annotation.ExceptionHandler;
import io.github.amings.mingle.svc.configuration.properties.SvcProperties;
import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.exception.handler.model.ExceptionModel;
import io.github.amings.mingle.svc.filter.SvcInfo;
import io.github.amings.mingle.svc.handler.SvcMsgHandler;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import io.github.amings.mingle.svc.utils.SvcResUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

/**
 * Exception or unknown Exception will be caught
 *
 * @author Ming
 */

@Slf4j
@ExceptionHandler
public class AllExceptionHandler extends AbstractExceptionHandler<Exception> {

    public AllExceptionHandler(SvcInfo svcInfo, SvcResUtils svcResUtils, SvcMsgHandler svcMsgHandler, SvcProperties svcProperties) {
        super(svcInfo, svcResUtils, svcMsgHandler, svcProperties);
    }

    @Override
    public ResponseEntity<SvcResModelHandler> handle(Exception e) {
        ExceptionModel model = new ExceptionModel();
        model.setException(e.getClass().getName());
        if (e.getCause() != null) {
            model.setCauseException(e.getCause().getClass().getName());
        }
        model.setMsg(e.getMessage());
        log.error("Exception by " + e);
        svcInfo.setSvcResModelHandler4Log(svcResUtils.build("MG01", "error", model));
        return build("MG01");
    }

}
