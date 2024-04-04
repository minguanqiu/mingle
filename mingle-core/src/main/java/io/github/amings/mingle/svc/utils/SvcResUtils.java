package io.github.amings.mingle.svc.utils;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.amings.mingle.svc.SvcResponse;
import io.github.amings.mingle.svc.handler.SvcResponseHandler;
import org.springframework.objenesis.SpringObjenesis;

/**
 * Utils for service response template
 *
 * @author Ming
 */
public class SvcResUtils {

    private final SpringObjenesis springObjenesis = new SpringObjenesis();
    private final SvcResponseHandler svcResponseHandler;
    private final JacksonUtils jacksonUtils;

    public SvcResUtils(SvcResponseHandler svcResponseHandler, JacksonUtils jacksonUtils) {
        this.svcResponseHandler = svcResponseHandler;
        this.jacksonUtils = jacksonUtils;
    }

    public SvcResponseHandler build(String code, String msg) {
        return build(code, msg, new SvcResponse());
    }

    public SvcResponseHandler build(String code, String msg, SvcResponse svcResponse) {
        return build(code, msg, svcResponse, jacksonUtils);
    }

    public SvcResponseHandler build(String code, String msg, SvcResponse svcResponse, JacksonUtils jacksonUtils) {
        return build(code, msg, jacksonUtils.readTree(svcResponse).orElse(null));
    }

    public SvcResponseHandler build(String code, String msg, JsonNode jsonNode) {
        SvcResponseHandler svcResponseHandlerImpl = springObjenesis.newInstance(svcResponseHandler.getClass(), true);
        svcResponseHandlerImpl.setCode(code);
        svcResponseHandlerImpl.setMsg(msg);
        svcResponseHandlerImpl.setResponseBody(jsonNode);
        return svcResponseHandlerImpl;
    }

}
