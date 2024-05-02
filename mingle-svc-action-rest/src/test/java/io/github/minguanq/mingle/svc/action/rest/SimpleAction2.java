package io.github.minguanq.mingle.svc.action.rest;

import io.github.minguanq.mingle.svc.action.ActionInterceptor;
import io.github.minguanq.mingle.svc.action.exception.resolver.ActionExceptionHandlerResolver;
import io.github.minguanq.mingle.svc.action.rest.configuration.properties.RestActionProperties;
import io.github.minguanq.mingle.svc.action.rest.handler.RestClientHandler;
import io.github.minguanq.mingle.svc.action.rest.utils.RestActionJacksonUtils;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ming
 */
@Service
public class SimpleAction2 extends AbstractSimpleAction<SimpleActionReq, SimpleActionRes> {

    public SimpleAction2(RestActionProperties actionProperties, ActionExceptionHandlerResolver actionExceptionHandlerResolver, List<ActionInterceptor> actionInterceptors, RestClientHandler restClientHandler, RestActionJacksonUtils restActionJacksonUtils) {
        super(actionProperties, actionExceptionHandlerResolver, actionInterceptors, restClientHandler, restActionJacksonUtils);
    }

    @Override
    protected List<String> buildRestPath(SimpleActionReq request) {
        return List.of();
    }

    @Override
    protected Map<String, Object> buildActionInfoValue(SimpleActionReq request, SimpleActionRes resBModel, Response response) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("httpCode", response.code());
        return stringObjectHashMap;
    }
}
