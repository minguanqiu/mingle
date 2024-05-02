package io.github.minguanq.mingle.svc.exception.handler;

import io.github.minguanq.mingle.svc.SvcResponseBody;
import io.github.minguanq.mingle.svc.SvcResponseHeader;
import io.github.minguanq.mingle.svc.exception.handler.model.AllExceptionModel;
import io.github.minguanq.mingle.svc.filter.SvcInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;

/**
 * Default handler will catch {@link Exception} or unknown exception
 *
 * @author Ming
 */
@Slf4j
@Order(Integer.MIN_VALUE)
public class AllExceptionHandler extends AbstractExceptionHandler<Exception> {

    public AllExceptionHandler(SvcInfo svcInfo) {
        super(svcInfo);
    }

    @Override
    public ResponseEntity<SvcResponseBody> handle(Exception e) {
        AllExceptionModel model = new AllExceptionModel();
        model.setException(e.getClass().getName());
        if (e.getCause() != null) {
            model.setCauseException(e.getCause().getClass().getName());
        }
        model.setMsg(e.getMessage());
        log.error("Exception by " + e);
        return build(SvcResponseHeader.builder("error").msg(e.getMessage()).build(), model);
    }

}
