package io.github.minguanqiu.mingle.svc.exception.handler;

import com.google.common.reflect.TypeToken;
import io.github.minguanqiu.mingle.svc.SvcResponseBody;
import io.github.minguanqiu.mingle.svc.SvcResponseHeader;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

/**
 * Base class for all exception handler
 *
 * @author Ming
 */
public abstract class AbstractExceptionHandler<E extends Exception> {

    protected final SvcInfo svcInfo;

    @Getter
    protected final Class<E> eClass;


    @SuppressWarnings("unchecked")
    public AbstractExceptionHandler(SvcInfo svcInfo) {
        this.svcInfo = svcInfo;
        eClass = (Class<E>) new TypeToken<E>(getClass()) {
        }.getRawType();
    }

    /**
     * Handle service exception logic
     *
     * @param ex Exception
     */
    public abstract ResponseEntity<SvcResponseBody> handle(E ex);


    /**
     * Return responseEntity for service response
     *
     * @param svcResponseHeader service response header
     */
    protected ResponseEntity<SvcResponseBody> build(SvcResponseHeader svcResponseHeader) {
        return build(svcResponseHeader, null);
    }

    /**
     * Return responseEntity for service response
     *
     * @param svcResponseHeader service response header
     * @param svcResponseBody       service response
     */
    protected ResponseEntity<SvcResponseBody> build(SvcResponseHeader svcResponseHeader, SvcResponseBody svcResponseBody) {
        svcInfo.setSvcResponseHeader(svcResponseHeader);
        return ResponseEntity.ok(svcResponseBody);
    }

}
