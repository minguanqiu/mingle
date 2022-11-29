package io.github.amings.mingle.svc.exception.handler.abs;

import com.google.common.reflect.TypeToken;
import io.github.amings.mingle.svc.SvcResModel;
import io.github.amings.mingle.svc.filter.SvcInfo;
import io.github.amings.mingle.svc.handler.SvcMsgHandler;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import io.github.amings.mingle.svc.utils.SvcResUtils;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

/**
 * Base class for all exception handler
 *
 * @author Ming
 */

public abstract class AbstractExceptionHandler<E extends Exception> {

    @Autowired
    public SvcInfo svcInfo;
    @Autowired
    public SvcResUtils svcResUtils;
    @Autowired
    public SvcMsgHandler svcMsgHandler;
    @Getter
    private final Class<E> eClass;
    @SuppressWarnings("unchecked")
    public AbstractExceptionHandler() {
        eClass = (Class<E>) new TypeToken<E>(getClass()) {}.getRawType();
    }

    /**
     * Handle exception logic
     * @param ex Exception
     * @return ResponseEntity
     */
    public abstract ResponseEntity<SvcResModelHandler> handle(E ex);

    /**
     * Build response body
     * @param code response code
     * @return ResponseEntity
     */
    protected ResponseEntity<SvcResModelHandler> build(String code) {
        return build(code, svcMsgHandler.getMsg(code));
    }

    /**
     * Build response body
     * @param code response code
     * @param desc response desc
     * @return ResponseEntity
     */
    protected ResponseEntity<SvcResModelHandler> build(String code, String desc) {
        return build(code, desc, new SvcResModel());
    }

    /**
     * Build response body
     * @param code response code
     * @param desc response desc
     * @param svcResModel Svc response model
     * @return ResponseEntity
     */
    protected ResponseEntity<SvcResModelHandler> build(String code, String desc, SvcResModel svcResModel) {
        return ResponseEntity.ok(svcResUtils.build(code, desc, svcResModel));
    }

}
