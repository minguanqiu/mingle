package io.github.minguanq.mingle.svc.utils;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.minguanq.mingle.svc.SvcResponseBody;
import io.github.minguanq.mingle.svc.handler.SvcResponseHandler;
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
        return build(code, msg, new SvcResponseBody());
    }

    public SvcResponseHandler build(String code, String msg, SvcResponseBody svcResponseBody) {
        return build(code, msg, svcResponseBody, jacksonUtils);
    }

    public SvcResponseHandler build(String code, String msg, SvcResponseBody svcResponseBody, JacksonUtils jacksonUtils) {
        return build(code, msg, jacksonUtils.readTree(svcResponseBody).orElse(null));
    }

    public SvcResponseHandler build(String code, String msg, JsonNode jsonNode) {
        SvcResponseHandler svcResponseHandlerImpl = springObjenesis.newInstance(svcResponseHandler.getClass(), true);
        svcResponseHandlerImpl.setCode(code);
        svcResponseHandlerImpl.setMsg(msg);
        svcResponseHandlerImpl.setResponseBody(jsonNode);
        return svcResponseHandlerImpl;
    }

}
