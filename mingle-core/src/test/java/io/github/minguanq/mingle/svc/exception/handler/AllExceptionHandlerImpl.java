package io.github.minguanq.mingle.svc.exception.handler;

import io.github.minguanq.mingle.svc.SvcResponseBody;
import io.github.minguanq.mingle.svc.SvcResponseHeader;
import io.github.minguanq.mingle.svc.TestUtils;
import io.github.minguanq.mingle.svc.exception.handler.model.TestExceptionModel;
import io.github.minguanq.mingle.svc.filter.SvcInfo;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author Ming
 */
@Component
@Profile("test-exception-handler")
public class AllExceptionHandlerImpl extends AbstractExceptionHandler<Exception> {
    public AllExceptionHandlerImpl(SvcInfo svcInfo) {
        super(svcInfo);
    }

    @Override
    public ResponseEntity<SvcResponseBody> handle(Exception ex) {
        return build(SvcResponseHeader.builder(TestUtils.X003).msg("X003-fail").build(), new TestExceptionModel("X003-fail"));
    }

}
