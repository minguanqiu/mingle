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
@Profile("add-exception-handler")
public class NullPointerExceptionHandlerImpl extends AbstractExceptionHandler<NullPointerException> {
    public NullPointerExceptionHandlerImpl(SvcInfo svcInfo) {
        super(svcInfo);
    }

    @Override
    public ResponseEntity<SvcResponseBody> handle(NullPointerException ex) {
        return build(SvcResponseHeader.builder(TestUtils.X004).msg("X004-fail").build(), new TestExceptionModel("X004-fail"));
    }

}
