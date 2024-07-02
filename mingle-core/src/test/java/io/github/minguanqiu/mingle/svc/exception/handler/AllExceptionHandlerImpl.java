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
 * @author Qiu Guan Ming
 */
@Component
@Profile("test-exception-handler")
public class AllExceptionHandlerImpl extends AbstractExceptionHandler<Exception> {

  public AllExceptionHandlerImpl(SvcInfo svcInfo) {
    super(svcInfo);
  }

  @Override
  public ResponseEntity<SvcResponseBody> handle(Exception ex) {
    return build(SvcResponseHeader.builder(SvcTestUtils.X003).msg("X003-fail").build(),
        new TestExceptionModel("X003-fail"));
  }

}
