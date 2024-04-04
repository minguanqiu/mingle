package io.github.amings.mingle.svc.exception.handler.abs;

import com.google.common.reflect.TypeToken;
import io.github.amings.mingle.svc.SvcResponse;
import io.github.amings.mingle.svc.SvcResponseHeader;
import io.github.amings.mingle.svc.filter.SvcInfo;
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


    public AbstractExceptionHandler(SvcInfo svcInfo) {
        this.svcInfo = svcInfo;
        eClass = (Class<E>) new TypeToken<E>(getClass()){}.getRawType();
    }

    /**
     * Handle service exception logic
     * @param ex Exception
     */
    public abstract ResponseEntity<SvcResponse> handle(E ex);

    /**
     * Return ResponseEntity for SvcResponse
     * @param code service response code
     */
    protected ResponseEntity<SvcResponse> build(String code) {
        return build(code, null);
    }

    /**
     * Return ResponseEntity for SvcResponse
     * @param code service response code
     * @param msg service response message
     */
    protected ResponseEntity<SvcResponse> build(String code, String msg) {
        return build(code, msg, new SvcResponse());
    }

    /**
     * Return ResponseEntity for SvcResponse
     * @param code service response code
     * @param msg service response message
     * @param svcResponse service response
     */
    protected ResponseEntity<SvcResponse> build(String code, String msg, SvcResponse svcResponse) {
        SvcResponseHeader svcResponseHeader = svcInfo.getSvcResponseHeader();
        svcResponseHeader.setCode(code);
        svcResponseHeader.setMsg(msg);
        return ResponseEntity.ok(svcResponse);
    }

}
