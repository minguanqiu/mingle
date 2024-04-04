package io.github.minguanq.mingle.svc.action.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.reflect.TypeToken;
import io.github.minguanq.mingle.svc.action.AbstractAction;
import io.github.minguanq.mingle.svc.action.ActionInfo;
import io.github.minguanq.mingle.svc.action.ActionInterceptor;
import io.github.minguanq.mingle.svc.action.ActionResponseBody;
import io.github.minguanq.mingle.svc.action.exception.resolver.ActionExceptionHandlerResolver;
import io.github.minguanq.mingle.svc.action.rest.annotation.BodyVariable;
import io.github.minguanq.mingle.svc.action.rest.annotation.ExcludeRequestBody;
import io.github.minguanq.mingle.svc.action.rest.annotation.PathVariable;
import io.github.minguanq.mingle.svc.action.rest.annotation.RestAction;
import io.github.minguanq.mingle.svc.action.rest.configuration.properties.RestActionProperties;
import io.github.minguanq.mingle.svc.action.rest.enums.HttpMethod;
import io.github.minguanq.mingle.svc.action.rest.exception.*;
import io.github.minguanq.mingle.svc.action.rest.handler.RestClientHandler;
import io.github.minguanq.mingle.svc.action.rest.utils.RestActionJacksonUtils;
import io.github.minguanq.mingle.svc.action.rest.utils.RestClientUtils;
import io.github.minguanq.mingle.svc.utils.StringUtils;
import okhttp3.*;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@inheritDoc}
 * <p>
 * Action of restful feature
 * <pre>
 * Feature :
 * Easy setting restful properties using {@link RestAction}
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

    protected final Environment environment;
    protected final RestActionProperties restActionProperties;
    protected final RestClientHandler restClientHandler;
    protected final RestActionJacksonUtils restActionJacksonUtils;
    private RestAction restAction;
    private Map<String, String> commonHeaderValueMap;
    private Set<Integer> successHttpCodeSet;
    private Class<ResB> resClass;

    public AbstractRestAction(RestActionProperties actionProperties, ActionExceptionHandlerResolver actionExceptionHandlerResolver, List<ActionInterceptor> actionInterceptors, Environment environment, RestClientHandler restClientHandler, RestActionJacksonUtils restActionJacksonUtils) {
        super(actionProperties.getActionProperties(), actionExceptionHandlerResolver, actionInterceptors);
        this.environment = environment;
        this.restActionProperties = actionProperties;
        this.restClientHandler = restClientHandler;
        this.restActionJacksonUtils = restActionJacksonUtils;
        init();
    }


    @Override
    protected final ActionInfo<ResB> processLogic(Req request, ActionInfo<ResB> actionInfo) {
        RestClientUtils restClientUtils = new RestClientUtils(getOkHttpClient(request));
        restClientUtils.setHttpMethod(restAction.method());
        buildHeader(restClientUtils, request);
        MediaType mediaType = MediaType.parse(restAction.mediaType());
        if (mediaType == null) {
            throw new MediaTypeParseErrorException("mediaType parse error,please check mediaType");
        }
        restClientUtils.setHeader("Content-Type", mediaType.toString());
        before(request);
        if (restClientUtils.getHttpMethod().equals(HttpMethod.GET)) {
            processGetMethodParam(restClientUtils, request);
        } else {
            restClientUtils.setRequestBody((buildRequestBody(mediaType, request)));
        }
        buildUri(restClientUtils, request);
        ResB resBModel;
        if (!checkMockExist()) {
            try (Response response = restClientUtils.call()) {
                actionInfo.setValues(buildResponseValueMap(response));
                checkHttpCode(response.code());
                resBModel = buildResponseBody(actionInfo, response.body().bytes());
            } catch (IOException e) {
                throw new ClientErrorException("client error : " + e.getMessage(), e);
            }
        } else {
            resBModel = processMockData(actionInfo);
        }
        if (actionProperties.getSuccessCode().equals(actionInfo.getCode()) && resBModel != null) {
            after(resBModel);
        }
        return actionInfo;
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
        return null;
    }

    /**
     * API response body format specification to json string
     *
     * @param responseBody API response body
     * @return JsonNode
     */
    protected JsonNode formatResponseBodyToJson(String responseBody) {
        return restActionJacksonUtils.readTree(responseBody).orElseThrow(() -> new ResponseBodyNotJsonFormatFailException("response body is not a json"));
    }


    /**
     * PreProcess action request model format
     *
     * @param request action request model
     */
    protected void before(Req request) {
    }

    /**
     * PostProcess action response model format
     *
     * @param responseBody action response model
     */
    protected void after(ResB responseBody) {
    }


    protected RestActionResponseBodyHeader buildResponseBodyHeader(JsonNode resultNode) {
        RestActionResponseBodyHeader restActionResHeaderModel = new RestActionResponseBodyHeader();
        restActionResHeaderModel.setSuccessCode(actionProperties.getSuccessCode());
        restActionResHeaderModel.setCode(actionProperties.getSuccessCode());
        restActionResHeaderModel.setMsg(actionProperties.getSuccessMsg());
        return restActionResHeaderModel;
    }


    private OkHttpClient getOkHttpClient(RestActionRequest reqModel) {
        if (reqModel.getOkHttpClientBuilder() != null) {
            return reqModel.getOkHttpClientBuilder().build();
        }
        return restClientHandler.getClient();
    }

    private void buildHeader(RestClientUtils restClientUtils, RestActionRequest reqModel) {
        if (reqModel.getHeaderValueMap() != null) {
            reqModel.getHeaderValueMap().forEach(restClientUtils::setHeader);
        } else {
            if (commonHeaderValueMap != null) {
                commonHeaderValueMap.forEach(restClientUtils::setHeader);
            }
        }
    }

    private void buildUri(RestClientUtils restClientUtils, Req reqModel) {
        HashMap<String, String> stringHashMap = null;
        for (Field field : reqModel.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            PathVariable annotation = field.getAnnotation(PathVariable.class);
            if (annotation != null) {
                if (stringHashMap == null) {
                    stringHashMap = new HashMap<>();
                }
                try {
                    if (!annotation.value().isEmpty()) {
                        stringHashMap.put(annotation.value(), (String) field.get(reqModel));
                    } else {
                        stringHashMap.put(field.getName(), (String) field.get(reqModel));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        String uri1 = environment.getProperty("mingle.svc.action.rest" + "." + getType() + ".uri");
        String uri2 = environment.getProperty("mingle.svc.action.rest" + "." + getType() + ".impl.uri." + this.getClass().getSimpleName());
        if (stringHashMap != null) {
            uri1 += StringUtils.templateConvert(uri2, stringHashMap, "{", "}");
        } else {
            uri1 += uri2;
        }
        restClientUtils.buildHttpUrl(uri1);
    }

    private void processGetMethodParam(RestClientUtils restClientUtils, Req reqModel) {
        restClientUtils.setQueryParameterMap(buildRequestBodyMap(reqModel));
    }

    @SuppressWarnings("unchecked")
    private <T> HashMap<String, T> buildRequestBodyMap(Req reqModel) {
        HashMap<String, T> stringHashMap = new HashMap<>();
        for (Field field : reqModel.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            BodyVariable bodyVariable = field.getAnnotation(BodyVariable.class);
            ExcludeRequestBody excludeRequestBody = field.getAnnotation(ExcludeRequestBody.class);
            try {
                if (excludeRequestBody == null) {
                    if (bodyVariable == null) {
                        stringHashMap.put(field.getName(), (T) field.get(reqModel));
                    } else {
                        stringHashMap.put(bodyVariable.value(), (T) field.get(reqModel));
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return stringHashMap;
    }

    /**
     * Build request body
     *
     * @param mediaType media type
     * @param reqModel  Action request model
     * @return RequestBody
     */
    protected RequestBody buildRequestBody(MediaType mediaType, Req reqModel) {
        if (mediaType.type().equals("multipart")) {
            if (mediaType.subtype().equals("form-data")) {
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(mediaType);
                HashMap<String, Object> map = buildRequestBodyMap(reqModel);
                map.forEach((k, v) -> {
                    if (v instanceof byte[]) {
                        builder.addFormDataPart(k, "", RequestBody.create((byte[]) v));
                    } else if (v instanceof String) {
                        builder.addFormDataPart(k, (String) v);
                    }
                });
                return builder.build();
            }
        } else if (mediaType.type().equals("application")) {
            if (mediaType.subtype().equals("x-www-form-urlencoded")) {
                HashMap<String, String> map = buildRequestBodyMap(reqModel);
                FormBody.Builder builder = new FormBody.Builder();
                map.forEach(builder::add);
                return builder.build();
            } else if (mediaType.subtype().equals("json")) {
                Optional<JsonNode> jsonNodeOptional = restActionJacksonUtils.readTree(reqModel);
                jsonNodeOptional.orElseThrow(() -> new ActionRequestSerializeErrorException("request model serialize error"));
                return RequestBody.create(jsonNodeOptional.get().toString().getBytes(StandardCharsets.UTF_8), mediaType);
            }
        }
        return null;
    }

    private boolean checkMockExist() {
        if (restActionProperties.getMockPath().isEmpty()) {
            return false;
        }
        return Files.exists(Paths.get(restActionProperties.getMockPath() + this.getClass().getSimpleName() + ".json"));
    }

    private void checkHttpCode(int code) {
        if (successHttpCodeSet != null) {
            if (!successHttpCodeSet.contains(code)) {
                throw new HttpCodeErrorException(code, "client code error : " + code);
            }
        }
    }

    private ResB processMockData(ActionInfo<ResB> actionInfo) {
        try (Stream<String> stream = Files.lines(new File(restActionProperties.getMockPath() + this.getClass().getSimpleName() + ".json").toPath())) {
            String res = stream.collect(Collectors.joining());
            Optional<JsonNode> jsonNodeOptional = restActionJacksonUtils.readTree(res);
            jsonNodeOptional.orElseThrow(Exception::new);
            JsonNode jsonNode = jsonNodeOptional.get();
            String sleep = jsonNode.get("sleep").asText();
            Thread.sleep(Long.parseLong(sleep));
            return buildResponseBody(actionInfo, jsonNode.get("data").toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new MockDataParseErrorException("mock data format error");
        }
    }

    protected Map<String, Object> buildResponseValueMap(Response response) {
        return null;
    }

    protected ResB buildResponseBody(ActionInfo<ResB> actionInfo, byte[] resBody) {
        JsonNode responseBodyNode = formatResponseBodyToJson(new String(resBody, StandardCharsets.UTF_8));
        ResB resBModel = deserializeResModel(responseBodyNode);
        RestActionResponseBodyHeader restActionResHeaderModel = buildResponseBodyHeader(responseBodyNode);
        if (!restActionResHeaderModel.getSuccessCode().equals(restActionResHeaderModel.getCode())) {
            actionInfo.setCode(restActionResHeaderModel.getCode());
            actionInfo.setMsg(restActionResHeaderModel.getMsg());
        }
        return resBModel;
    }

    protected ResB deserializeResModel(JsonNode resultNode) {
        return restActionJacksonUtils.readValue(resultNode.toString(), resClass).orElseThrow(() -> new ActionResponseBodyDeserializeErrorException("resModel deserialize error"));
    }

    @SuppressWarnings("unchecked")
    private void init() {
        successHttpCodeSet = buildSuccessHttpCodeSet();
        commonHeaderValueMap = buildCommonHeaderValue();
        restAction = this.getClass().getAnnotation(RestAction.class);
        resClass = (Class<ResB>) new TypeToken<ResB>(getClass()) {
        }.getRawType();
    }


}
