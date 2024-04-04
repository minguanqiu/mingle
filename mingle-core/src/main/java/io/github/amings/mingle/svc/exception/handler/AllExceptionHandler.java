package io.github.amings.mingle.svc.exception.handler;

import io.github.amings.mingle.svc.SvcResponse;
import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.exception.handler.model.AllExceptionModel;
import io.github.amings.mingle.svc.filter.SvcInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Default handler will catch {@link Exception} or unknown exception
 *
 * @author Ming
 */
@Slf4j
@Component
@Order(Integer.MIN_VALUE)
public class AllExceptionHandler extends AbstractExceptionHandler<Exception> {

    public AllExceptionHandler(SvcInfo svcInfo) {
        super(svcInfo);
    }

    @Override
    public ResponseEntity<SvcResponse> handle(Exception e) {
        AllExceptionModel model = new AllExceptionModel();
        model.setException(e.getClass().getName());
        if (e.getCause() != null) {
            model.setCauseException(e.getCause().getClass().getName());
        }
        model.setMsg(e.getMessage());
        log.error("Exception by " + e);
        return build("error", "", model);
    }

}
