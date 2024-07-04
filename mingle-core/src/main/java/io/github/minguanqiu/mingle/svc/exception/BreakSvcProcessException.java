package io.github.minguanqiu.mingle.svc.exception;

import io.github.minguanqiu.mingle.svc.SvcResponseBody;
import io.github.minguanqiu.mingle.svc.SvcResponseHeader;
import lombok.Getter;

/**
 * Exception for break service process logic
 *
 * @author Qiu Guan Ming
 */

@Getter
public class BreakSvcProcessException extends RuntimeException {

  /**
   * Service response header.
   *
   * @return return the service response header.
   */
  private final SvcResponseHeader svcResponseHeader;

  /**
   * Service response body.
   *
   * @return return the service response body.
   */
  private final SvcResponseBody svcResponseBody;

  /**
   * Create a new BreakSvcProcessException instance.
   *
   * @param svcResponseHeader the service response header.
   * @param svcResponseBody   the service response body.
   */
  public BreakSvcProcessException(SvcResponseHeader svcResponseHeader,
      SvcResponseBody svcResponseBody) {
    this.svcResponseHeader = svcResponseHeader;
    this.svcResponseBody = svcResponseBody;
  }

}
