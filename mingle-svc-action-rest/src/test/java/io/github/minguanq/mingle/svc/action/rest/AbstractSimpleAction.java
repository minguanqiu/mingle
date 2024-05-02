package io.github.minguanq.mingle.svc.action.rest;

import io.github.minguanq.mingle.svc.action.ActionInterceptor;
import io.github.minguanq.mingle.svc.action.ActionResponseBody;
import io.github.minguanq.mingle.svc.action.exception.resolver.ActionExceptionHandlerResolver;
import io.github.minguanq.mingle.svc.action.rest.configuration.properties.RestActionProperties;
import io.github.minguanq.mingle.svc.action.rest.enums.HttpMethod;
import io.github.minguanq.mingle.svc.action.rest.handler.RestClientHandler;
import io.github.minguanq.mingle.svc.action.rest.utils.RestActionJacksonUtils;

import java.util.List;

/**
 * @author Ming
 */
public abstract class AbstractSimpleAction<Req extends RestActionRequest, ResB extends ActionResponseBody> extends AbstractRestAction<Req, ResB> {

    public AbstractSimpleAction(RestActionProperties actionProperties, ActionExceptionHandlerResolver actionExceptionHandlerResolver, List<ActionInterceptor> actionInterceptors, RestClientHandler restClientHandler, RestActionJacksonUtils restActionJacksonUtils) {
        super(actionProperties, actionExceptionHandlerResolver, actionInterceptors, restClientHandler, restActionJacksonUtils);
    }

    @Override
    HttpMethod buildHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    String getServerName() {
        return "Simple";
    }

    @Override
    public String getType() {
        return "Simple";
    }

}
