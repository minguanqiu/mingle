package io.github.minguanqiu.mingle.svc.exception.handler;

import io.github.minguanqiu.mingle.svc.SvcResponseBody;
import io.github.minguanqiu.mingle.svc.SvcResponseHeader;
import io.github.minguanqiu.mingle.svc.SvcTestUtils;
import io.github.minguanqiu.mingle.svc.exception.handler.model.TestExceptionModel;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
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
        return build(SvcResponseHeader.builder(SvcTestUtils.X004).msg("X004-fail").build(), new TestExceptionModel("X004-fail"));
    }

}
