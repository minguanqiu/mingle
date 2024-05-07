package io.github.minguanqiu.mingle.svc.action.rest;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.minguanqiu.mingle.svc.action.ActionInterceptor;
import io.github.minguanqiu.mingle.svc.action.exception.resolver.ActionExceptionHandlerResolver;
import io.github.minguanqiu.mingle.svc.action.rest.configuration.properties.RestActionProperties;
import io.github.minguanqiu.mingle.svc.action.rest.exception.ActionResponseBodyDeserializeErrorException;
import io.github.minguanqiu.mingle.svc.action.rest.handler.RestClientHandler;
import io.github.minguanqiu.mingle.svc.action.rest.utils.RestActionJacksonUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ming
 */
@Service
public class SimpleAction1 extends AbstractSimpleAction<SimpleActionReq, SimpleActionRes> {

    public SimpleAction1(RestActionProperties actionProperties, ActionExceptionHandlerResolver actionExceptionHandlerResolver, List<ActionInterceptor> actionInterceptors, RestClientHandler restClientHandler, RestActionJacksonUtils restActionJacksonUtils) {
        super(actionProperties, actionExceptionHandlerResolver, actionInterceptors, restClientHandler, restActionJacksonUtils);
    }

    @Override
    protected List<String> buildRestPath(SimpleActionReq request) {
        return List.of("SimpleSvc");
    }

    @Override
    protected SimpleActionRes deserializeResponseBody(JsonNode resultNode) {
        return restActionJacksonUtils.readValue(resultNode.get("body").toString(), resClass).orElseThrow(ActionResponseBodyDeserializeErrorException::new);
    }

}
