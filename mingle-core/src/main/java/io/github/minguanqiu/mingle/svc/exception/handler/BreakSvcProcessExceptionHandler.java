package io.github.minguanqiu.mingle.svc.exception.handler;

import io.github.minguanqiu.mingle.svc.SvcResponseBody;
import io.github.minguanqiu.mingle.svc.exception.BreakSvcProcessException;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;

/**
 * Default handler will catch {@link BreakSvcProcessException}
 *
 * @author Qiu Guan Ming
 */
@Order(Integer.MIN_VALUE)
public class BreakSvcProcessExceptionHandler extends
    AbstractExceptionHandler<BreakSvcProcessException> {

  /**
   * Create a new BreakSvcProcessExceptionHandler instance.
   *
   * @param svcInfo the service information.
   */
  public BreakSvcProcessExceptionHandler(SvcInfo svcInfo) {
    super(svcInfo);
  }

  @Override
  public ResponseEntity<SvcResponseBody> handle(BreakSvcProcessException e) {
    return build(e.getSvcResponseHeader(), e.getSvcResponseBody());
  }

}
