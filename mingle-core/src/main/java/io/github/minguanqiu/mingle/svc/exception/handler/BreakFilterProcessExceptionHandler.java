package io.github.minguanqiu.mingle.svc.exception.handler;

import io.github.minguanqiu.mingle.svc.SvcResponseBody;
import io.github.minguanqiu.mingle.svc.SvcResponseHeader;
import io.github.minguanqiu.mingle.svc.exception.BreakFilterProcessException;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;

/**
 * Default handler will catch {@link BreakFilterProcessException}
 *
 * @author Qiu Guan Ming
 */
@Order(Integer.MIN_VALUE)
public class BreakFilterProcessExceptionHandler extends
    AbstractExceptionHandler<BreakFilterProcessException> {

  /**
   * Create a new BreakFilterProcessExceptionHandler instance.
   *
   * @param svcInfo the service information.
   */
  public BreakFilterProcessExceptionHandler(SvcInfo svcInfo) {
    super(svcInfo);
  }

  @Override
  public ResponseEntity<SvcResponseBody> handle(BreakFilterProcessException e) {
    return build(SvcResponseHeader.builder(e.getCode()).msg(e.getMsg()).build());
  }

}
