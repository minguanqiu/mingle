package io.github.minguanqiu.mingle.svc.exception;

import io.github.minguanqiu.mingle.svc.SvcResponseBody;
import io.github.minguanqiu.mingle.svc.SvcResponseHeader;
import lombok.Getter;

/**
 * Exception for break service process logic
 *
 * @author Ming
 */

@Getter
public class BreakSvcProcessException extends RuntimeException {

    private final SvcResponseHeader svcResponseHeader;

    private final SvcResponseBody svcResponseBody;

    /**
     * @param svcResponseHeader service response header
     * @param svcResponseBody       service response body
     */
    public BreakSvcProcessException(SvcResponseHeader svcResponseHeader, SvcResponseBody svcResponseBody) {
        this.svcResponseHeader = svcResponseHeader;
        this.svcResponseBody = svcResponseBody;
    }

}
