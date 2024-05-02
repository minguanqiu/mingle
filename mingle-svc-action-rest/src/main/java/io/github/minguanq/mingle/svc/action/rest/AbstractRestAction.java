package io.github.minguanq.mingle.svc.action.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.reflect.TypeToken;
import io.github.minguanq.mingle.svc.action.AbstractAction;
import io.github.minguanq.mingle.svc.action.ActionInfo;
import io.github.minguanq.mingle.svc.action.ActionInterceptor;
import io.github.minguanq.mingle.svc.action.ActionResponseBody;
import io.github.minguanq.mingle.svc.action.exception.resolver.ActionExceptionHandlerResolver;
import io.github.minguanq.mingle.svc.action.rest.annotation.PathVariable;
import io.github.minguanq.mingle.svc.action.rest.configuration.properties.RestActionProperties;
import io.github.minguanq.mingle.svc.action.rest.enums.HttpMethod;
import io.github.minguanq.mingle.svc.action.rest.exception.*;
import io.github.minguanq.mingle.svc.action.rest.handler.RestClientHandler;
import io.github.minguanq.mingle.svc.action.rest.utils.RestActionJacksonUtils;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * {@inheritDoc}
 * <p>
 * Action of restful feature
 * <pre>
 * Feature :
 * URI via spring profile define
 * Request serialize and response deserialize using jackson
 * Target response body header common process to decide action success or fail
 *
 * </pre>
 *
 * @author Ming
 */

public abstract class AbstractRestAction<Req extends RestActionRequest, ResB extends ActionResponseBody>
        extends AbstractAction<Req, ResB> {

    protected final RestActionProperties restActionProperties;
    protected final RestClientHandler restClientHandler;
    protected final RestActionJacksonUtils restActionJacksonUtils;
    private Map<String, String> commonHeaderValueMap;
    private Set<Integer> successHttpCodeSet;
    protected final Class<ResB> resClass;

    @SuppressWarnings("unchecked")
    public AbstractRestAction(RestActionProperties actionProperties, ActionExceptionHandlerResolver actionExceptionHandlerResolver, List<ActionInterceptor> actionInterceptors, RestClientHandler restClientHandler, RestActionJacksonUtils restActionJacksonUtils) {
        super(actionProperties, actionExceptionHandlerResolver, actionInterceptors);
        this.restActionProperties = actionProperties;
        this.restClientHandler = restClientHandler;
        this.restActionJacksonUtils = restActionJacksonUtils;
        resClass = (Class<ResB>) new TypeToken<ResB>(getClass()) {
        }.getRawType();
        init();
    }


    @Override
    protected final ResB processLogic(Req request, ActionInfo actionInfo) {
        before(request);
        ResponseData responseData = !restActionProperties.getRest().getMock().containsKey(getMockName()) ? processResponse(request) : processMockResponse();
        ResB responseBody = buildResponseBody(actionInfo, responseData.responseBytes());
        actionInfo.getValues().putAll(buildActionInfoValue(request, responseBody, responseData.response()));
        checkHttpCode(responseData.response().code());
        if (svcActionProperties.getCode().equals(actionInfo.getCode()) && responseBody != null) {
            after(responseBody);
        }
        return responseBody;
    }

    private ResponseData processResponse(Req request) {
        try (Response response = getOkHttpClient(request).newCall(buildHttpRequest(request)).execute()) {
            if (response.cacheResponse() != null) {
                return new ResponseData(response.cacheResponse(), response.body().bytes());
            }
            return new ResponseData(response, response.body().bytes());
        } catch (IOException e) {
            throw new ClientErrorException("client error : " + e.getMessage(), e);
        }
    }

    private ResponseData processMockResponse() {
        RestActionProperties.RestProperties.MockProperties mockProperties = restActionProperties.getRest().getMock().get(getMockName());
        Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(mockProperties.getCode());
        mockProperties.getHeader().forEach(responseBuilder::addHeader);
        responseBuilder.body(ResponseBody.create(mockProperties.getResponseBody().getMediaType(), MediaType.parse(mockProperties.getResponseBody().getContent())));
        responseBuilder.message(mockProperties.getMessage());
        try (Response response = responseBuilder.build()) {
            Thread.sleep(mockProperties.getDelay());
            return new ResponseData(response, response.body().bytes());
        } catch (Exception e) {
            throw new MockErrorException(e);
        }
    }

    protected Map<String, Object> buildActionInfoValue(Req request, ResB resBModel, Response response) {
        return null;
    }

    protected Request buildHttpRequest(Req request) {
        Request.Builder requestBuilder = new Request.Builder();
        if (commonHeaderValueMap != null) {
            commonHeaderValueMap.forEach(requestBuilder::addHeader);
        }
        requestBuilder.url(buildUrl(request));
        if (buildHttpMethod().equals(HttpMethod.GET)) {
            requestBuilder.get();
        } else {
            requestBuilder.method(buildHttpMethod().name(), buildRequestBody(request));
        }
        return requestBuilder.build();
    }

    abstract HttpMethod buildHttpMethod();

    abstract protected List<String> buildRestPath(Req request);

    abstract String getServerName();

    public String getMockName() {
        return null;
    }

    /**
     * Build action request header for cache
     *
     * @return Map
     */
    protected Map<String, String> buildCommonHeaderValue() {
        return null;
    }

    /**
     * Build action success http code for cache,if not contains code,this action will set error code and not success
     *
     * @return Set
     */
    protected Set<Integer> buildSuccessHttpCodeSet() {
        return Set.of(200);
    }

    /**
     * API response body format specification to json string
     *
     * @param responseBody API response body
     * @return JsonNode
     */
    protected JsonNode formatRawResponseBody(String responseBody) {
        return restActionJacksonUtils.readTree(responseBody).orElseThrow(ResponseBodyNotJsonFormatFailException::new);
    }


    /**
     * Pre process action request
     *
     * @param request action request model
     */
    protected void before(Req request) {
    }

    /**
     * Post process action response body
     *
     * @param responseBody action response model
     */
    protected void after(ResB responseBody) {
    }


    protected RestActionResponseBodyHeader buildResponseBodyHeader(JsonNode resultNode) {
        return null;
    }


    private OkHttpClient getOkHttpClient(RestActionRequest request) {
        if (request.getOkHttpClientBuilder() != null) {
            return request.getOkHttpClientBuilder().build();
        }
        return restClientHandler.getClient();
    }


    protected HttpUrl buildUrl(Req request) {
        if (!restActionProperties.getRest().getServer().containsKey(getServerName())) {
            throw new ServerNotExistException("Server not exist with name: " + getServerName());
        }
        RestActionProperties.RestProperties.ServerProperties serverProperties = restActionProperties.getRest().getServer().get(getServerName());
        List<String> restPaths = buildRestPath(request);
        HttpUrl.Builder builder = new HttpUrl.Builder().scheme(serverProperties.getScheme()).host(serverProperties.getHost()).port(serverProperties.getPort());
        for (String pathSegment : serverProperties.getPathSegments()) {
            builder.addPathSegments(pathSegment);
        }
        if (restPaths != null) {
            restPaths.forEach(builder::addPathSegment);
        }
        HashMap<String, String> pathVariable = buildPathVariable(request);
        if (pathVariable != null) {
            pathVariable.forEach(builder::addQueryParameter);
        }
        return builder.build();
    }

    protected HashMap<String, String> buildPathVariable(Req request) {
        HashMap<String, String> stringHashMap = null;
        for (Field field : request.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            PathVariable annotation = field.getAnnotation(PathVariable.class);
            if (annotation != null) {
                if (stringHashMap == null) {
                    stringHashMap = new HashMap<>();
                }
                try {
                    if (!annotation.value().isEmpty()) {
                        stringHashMap.put(annotation.value(), (String) field.get(request));
                    } else {
                        stringHashMap.put(field.getName(), (String) field.get(request));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return stringHashMap;
    }

    /**
     * Build request body
     *
     * @param reqModel Action request model
     * @return RequestBody
     */
    protected RequestBody buildRequestBody(Req reqModel) {
        Optional<JsonNode> jsonNodeOptional = restActionJacksonUtils.readTree(reqModel);
        jsonNodeOptional.orElseThrow(ActionRequestSerializeErrorException::new);
        return RequestBody.create(jsonNodeOptional.get().toString().getBytes(StandardCharsets.UTF_8), MediaType.parse("application/json"));
    }


    private void checkHttpCode(int code) {
        if (successHttpCodeSet != null) {
            if (!successHttpCodeSet.contains(code)) {
                throw new HttpCodeErrorException(code, "client code error : " + code);
            }
        }
    }

    private ResB buildResponseBody(ActionInfo actionInfo, byte[] resBody) {
        JsonNode responseBodyNode = formatRawResponseBody(new String(resBody, StandardCharsets.UTF_8));
        RestActionResponseBodyHeader restActionResHeaderModel = buildResponseBodyHeader(responseBodyNode);
        if (restActionResHeaderModel != null && !restActionResHeaderModel.getSuccessCode().equals(restActionResHeaderModel.getCode())) {
            actionInfo.setCode(restActionResHeaderModel.getCode());
            actionInfo.setMsg(restActionResHeaderModel.getMsg());
        }
        return deserializeResponseBody(responseBodyNode);
    }

    protected ResB deserializeResponseBody(JsonNode resultNode) {
        return restActionJacksonUtils.readValue(resultNode.toString(), resClass).orElseThrow(ActionResponseBodyDeserializeErrorException::new);
    }

    private void init() {
        successHttpCodeSet = buildSuccessHttpCodeSet();
        commonHeaderValueMap = buildCommonHeaderValue();
    }


    private record ResponseData(Response response, byte[] responseBytes) {
    }

}
