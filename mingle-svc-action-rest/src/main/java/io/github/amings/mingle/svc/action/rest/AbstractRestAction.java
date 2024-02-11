package io.github.amings.mingle.svc.action.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.reflect.TypeToken;
import io.github.amings.mingle.svc.action.AbstractAction;
import io.github.amings.mingle.svc.action.exception.resolver.ActionExceptionHandlerResolver;
import io.github.amings.mingle.svc.action.interceptor.ActionInterceptor;
import io.github.amings.mingle.svc.action.rest.annotation.DataProperty;
import io.github.amings.mingle.svc.action.rest.annotation.ExcludeRequestBody;
import io.github.amings.mingle.svc.action.rest.annotation.PathVariable;
import io.github.amings.mingle.svc.action.rest.annotation.RestAction;
import io.github.amings.mingle.svc.action.rest.configuration.properties.RestActionProperties;
import io.github.amings.mingle.svc.action.rest.exception.*;
import io.github.amings.mingle.svc.action.rest.handler.RestClientHandler;
import io.github.amings.mingle.utils.FileUtils;
import io.github.amings.mingle.utils.JacksonUtils;
import io.github.amings.mingle.utils.RestClientUtils;
import io.github.amings.mingle.utils.StringUtils;
import io.github.amings.mingle.utils.enums.HttpMethod;
import okhttp3.*;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Base class for all rest client action logic class
 *
 * @author Ming
 */

public abstract class AbstractRestAction<Req extends RestActionReqModel, ResData extends RestActionResData, Res extends RestActionResModel>
        extends AbstractAction<Req, ResData, Res> {

    protected final Environment environment;
    protected final RestActionProperties restActionProperties;
    protected final RestClientHandler restClientHandler;
    protected final JacksonUtils jacksonUtils;
    private RestAction restAction;
    private Map<String, String> commonHeaderValueMap;
    private Set<Integer> successHttpCodeSet;
    private Class<Res> resClass;

    public AbstractRestAction(RestActionProperties actionProperties, ActionExceptionHandlerResolver actionExceptionHandlerResolver, List<ActionInterceptor> actionInterceptors, Environment environment, RestClientHandler restClientHandler, JacksonUtils jacksonUtils) {
        super(actionProperties.getActionProperties(), actionExceptionHandlerResolver, actionInterceptors);
        this.environment = environment;
        this.restActionProperties = actionProperties;
        this.restClientHandler = restClientHandler;
        this.jacksonUtils = jacksonUtils;
        init();
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
    protected JsonNode formatResponseBody(String responseBody) {
        return jacksonUtils.readTree(responseBody).orElseThrow(() -> new ResponseBodyFormatFailException("response body is not a json"));
    }

    /**
     * Process API response body will return value and deserialize to action response model
     *
     * @param responseBody jsonNode
     * @return JsonNode
     */
    protected JsonNode processResponseBody(JsonNode responseBody) {
        return responseBody;
    }

    /**
     * PreProcess action request model format
     *
     * @param reqModel action request model
     */
    protected void before(Req reqModel) {
    }

    /**
     * PostProcess action response model format
     *
     * @param resModel action response model
     */
    protected void after(Res resModel) {
    }


    protected RestActionResHeaderModel buildResHeaderModel(JsonNode resultNode) {
        RestActionResHeaderModel restActionResHeaderModel = new RestActionResHeaderModel();
        restActionResHeaderModel.setSuccessCode(actionProperties.getSuccessCode());
        restActionResHeaderModel.setCode(actionProperties.getSuccessCode());
        restActionResHeaderModel.setDesc(actionProperties.getSuccessDesc());
        return restActionResHeaderModel;
    }

    /**
     * Process rest action logic
     *
     * @param reqModel Action request model
     * @param resData
     * @return Res Action response model
     */
    @Override
    protected final Res processLogic(Req reqModel, ResData resData) {
        RestClientUtils restClientUtils = new RestClientUtils(getOkHttpClient(reqModel));
        restClientUtils.setHttpMethod(restAction.method());
        buildHeader(restClientUtils, reqModel);
        MediaType mediaType = MediaType.parse(restAction.mediaType());
        if (mediaType == null) {
            throw new MediaTypeParseFailException("mediaType parse fail,please check mediaType");
        }
        restClientUtils.setHeader("Content-Type", mediaType.toString());
        before(reqModel);
        if (restClientUtils.getHttpMethod().equals(HttpMethod.GET)) {
            processGetMethodParam(restClientUtils, reqModel);
        } else {
            restClientUtils.setRequestBody((buildRequestBody(mediaType, reqModel)));
        }
        buildUri(restClientUtils, reqModel);
        resData.setUri(restClientUtils.getHttpUrl().toString());
        Res resModel;
        if (!checkMockExist()) {
            try (Response response = restClientUtils.call()) {
                buildResData(resData, response);
                checkHttpCode(response.code());
                resModel = buildResModel(response.body().bytes());
            } catch (IOException e) {
                throw new ClientErrorException("client error : " + e.getMessage(), e);
            }
        } else {
            resModel = processMockData();
        }
        if (resModel != null) {
            after(resModel);
        }
        return resModel;
    }

    private OkHttpClient getOkHttpClient(RestActionReqModel reqModel) {
        if (reqModel.getOkHttpClientBuilder() != null) {
            return reqModel.getOkHttpClientBuilder().build();
        }
        return restClientHandler.getClient();
    }

    private void buildHeader(RestClientUtils restClientUtils, RestActionReqModel reqModel) {
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
            DataProperty dataProperty = field.getAnnotation(DataProperty.class);
            ExcludeRequestBody excludeRequestBody = field.getAnnotation(ExcludeRequestBody.class);
            try {
                if (excludeRequestBody == null) {
                    if (dataProperty == null) {
                        stringHashMap.put(field.getName(), (T) field.get(reqModel));
                    } else {
                        stringHashMap.put(dataProperty.value(), (T) field.get(reqModel));
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
                Optional<JsonNode> jsonNodeOptional = jacksonUtils.readTree(reqModel);
                jsonNodeOptional.orElseThrow(() -> new ActionReqModelSerializeFailException("request model serialize fail"));
                return RequestBody.create(jsonNodeOptional.get().toString().getBytes(StandardCharsets.UTF_8), mediaType);
            }
        }
        return null;
    }

    private boolean checkMockExist() {
        if (restActionProperties.getMockPath().isEmpty()) {
            return false;
        }
        return FileUtils.isExist(restActionProperties.getMockPath() + this.getClass().getSimpleName() + ".json");
    }

    private void checkHttpCode(int code) {
        if (successHttpCodeSet != null) {
            if (!successHttpCodeSet.contains(code)) {
                throw new HttpCodeErrorException(code, "client code error : " + code);
            }
        }
    }

    private Res processMockData() {
        try (Stream<String> stream = FileUtils.readFile(restActionProperties.getMockPath() + this.getClass().getSimpleName() + ".json")) {
            String res = stream.collect(Collectors.joining());
            Optional<JsonNode> jsonNodeOptional = jacksonUtils.readTree(res);
            jsonNodeOptional.orElseThrow(Exception::new);
            JsonNode jsonNode = jsonNodeOptional.get();
            String sleep = jsonNode.get("sleep").asText();
            Thread.sleep(Long.parseLong(sleep));
            return buildResModel(jsonNode.get("data").toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new MockDataParseFailException("mock data format error");
        }
    }

    protected void buildResData(ResData resData, Response response) {
        resData.setHttpCode(String.valueOf(response.code()));
        resData.setResponseHeaderValue(response.headers().toMultimap());
    }

    protected Res buildResModel(byte[] resBody) {
        JsonNode responseBodyNode = formatResponseBody(new String(resBody, StandardCharsets.UTF_8));
        Res resModel = deserializeResModel(responseBodyNode);
        RestActionResHeaderModel restActionResHeaderModel = buildResHeaderModel(responseBodyNode);
        if (!restActionResHeaderModel.getSuccessCode().equals(restActionResHeaderModel.getCode())) {
            breakActionLogic(restActionResHeaderModel.getCode(), restActionResHeaderModel.getDesc(), resModel);
        }
        return resModel;
    }

    private Res deserializeResModel(JsonNode resultNode) {
        return jacksonUtils.readValue(processResponseBody(resultNode).toString(), resClass).orElseThrow(() -> new ActionResModelFormatErrorException("resModel format error"));
    }

    @SuppressWarnings("unchecked")
    private void init() {
        successHttpCodeSet = buildSuccessHttpCodeSet();
        commonHeaderValueMap = buildCommonHeaderValue();
        restAction = this.getClass().getAnnotation(RestAction.class);
        resClass = (Class<Res>) new TypeToken<Res>(getClass()) {
        }.getRawType();
    }


}
