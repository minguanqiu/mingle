package io.github.amings.mingle.svc.exception.handler.abs;

import io.github.amings.mingle.svc.SvcResModel;
import io.github.amings.mingle.svc.config.properties.SvcProperties;
import io.github.amings.mingle.svc.filter.SvcInfo;
import io.github.amings.mingle.svc.handler.SvcMsgHandler;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import io.github.amings.mingle.svc.utils.SvcResUtils;
import org.springframework.http.ResponseEntity;

/**
 * Base class for all exception handler
 *
 * @author Ming
 */

public abstract class AbstractExceptionHandler<E extends Exception> {

    protected final SvcInfo svcInfo;
    protected final SvcResUtils svcResUtils;
    protected final SvcMsgHandler svcMsgHandler;
    protected final SvcProperties svcProperties;


    public AbstractExceptionHandler(SvcInfo svcInfo, SvcResUtils svcResUtils, SvcMsgHandler svcMsgHandler, SvcProperties svcProperties) {
        this.svcInfo = svcInfo;
        this.svcResUtils = svcResUtils;
        this.svcMsgHandler = svcMsgHandler;
        this.svcProperties = svcProperties;
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
        return build(code, svcMsgHandler.getMsg(svcProperties.getMsgType(), code));
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
