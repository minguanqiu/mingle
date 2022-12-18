package io.github.amings.mingle.svc.action.rest;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.amings.mingle.svc.action.AbstractAction;
import io.github.amings.mingle.svc.action.ActionReqModel;
import io.github.amings.mingle.svc.action.ActionResModel;
import io.github.amings.mingle.svc.action.exception.BreakActionException;
import io.github.amings.mingle.svc.action.exception.BreakActionLogicException;
import io.github.amings.mingle.svc.action.rest.annotation.DataProperty;
import io.github.amings.mingle.svc.action.rest.annotation.ExcludeRequestBody;
import io.github.amings.mingle.svc.action.rest.annotation.PathVariable;
import io.github.amings.mingle.svc.action.rest.annotation.RestAction;
import io.github.amings.mingle.svc.action.rest.exception.ActionReqModelSerializeFailException;
import io.github.amings.mingle.svc.action.rest.exception.ActionResModelFormatErrorException;
import io.github.amings.mingle.svc.action.rest.exception.ActionRestResModelFormatFailException;
import io.github.amings.mingle.svc.action.rest.exception.ClientErrorException;
import io.github.amings.mingle.svc.action.rest.exception.HttpCodeErrorException;
import io.github.amings.mingle.svc.action.rest.exception.MediaTypeParseFailException;
import io.github.amings.mingle.svc.action.rest.exception.MockDataParseFailException;
import io.github.amings.mingle.svc.action.rest.handler.RestClientHandler;
import io.github.amings.mingle.utils.FileUtils;
import io.github.amings.mingle.utils.JacksonUtils;
import io.github.amings.mingle.utils.RestClientUtils;
import io.github.amings.mingle.utils.StringUtils;
import io.github.amings.mingle.utils.enums.HttpMethod;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Base class for all rest client action logic class
 *
 * @author Ming
 */

public abstract class AbstractRestAction<Req extends ActionReqModel, Res extends ActionResModel>
        extends AbstractAction<Req, Res, RestActionReqData, RestActionResData<Res>> {

    @Autowired
    Environment environment;
    @Autowired
    RestClientHandler restClientHandler;
    @Autowired
    @Qualifier("restActionJacksonUtils")
    protected JacksonUtils jacksonUtils;
    private RestAction restAction;
    @Value("${mingle.svc.action.rest.mock.path:}")
    private String MOCK_PATH;
    private Map<String, String> cacheHeaderValueMap;
    private Set<Integer> cacheSuccessHttpCodeList;


    /**
     * Build action request header for cache
     *
     * @return Map
     */
    protected abstract Map<String, String> buildRequestCacheHeaderValue();

    /**
     * Build action success http code for cache,if not contains code,this action will set error code and not success
     *
     * @return Set
     */
    protected abstract Set<Integer> buildCacheSuccessHttpCode();

    /**
     * API response body format specification to json string
     *
     * @param responseBody API response body
     * @return JsonNode
     */
    protected JsonNode formatResponseBody(String responseBody) {
        return jacksonUtils.readTree(responseBody).get();
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
    protected abstract void before(Req reqModel);

    /**
     * PostProcess action response model format
     *
     * @param resModel action response model
     */
    protected abstract void after(Res resModel);

    /**
     * Defined target API common response body to check success result,default by no restResModel
     *
     * @return Class
     */
    protected Class<? extends RestActionResModel> getActionRestResModelClass() {
        return null;
    }


    /**
     * Process rest action logic
     *
     * @param reqModel Action request model
     * @param reqData  Action request data
     * @param resData  Action response data
     * @return Res Action response model
     */
    @Override
    protected final Res processAction(Req reqModel, RestActionReqData reqData, RestActionResData<Res> resData) throws BreakActionException {
        RestClientUtils restClientUtils = new RestClientUtils(getOkHttpClient(reqData));
        restClientUtils.setHttpMethod(restAction.method());
        buildHeader(restClientUtils, reqData);
        before(reqModel);
        if (restClientUtils.getHttpMethod().equals(HttpMethod.GET)) {
            processGetMethodParam(restClientUtils, reqModel);
        } else {
            restClientUtils.setRequestBody((buildRequestBody(reqModel)));
        }
        buildUri(restClientUtils, reqModel);
        resData.setUri(restClientUtils.getHttpUrl().toString());
        Res resModel = null;
        if (!checkMockExist()) {
            try (Response response = restClientUtils.call()) {
                resData.setHttpCode(resData.getHttpCode());
                resData.setResponseHeaderValue(response.headers().toMultimap());
                checkHttpCode(response.code());
                resModel = processResBody(resData, response.body().bytes());
            } catch (IOException e) {
                throw new ClientErrorException("client error : " + e.getMessage(), e);
            }
        } else {
            processMockData(resData);
        }
        if (resModel != null) {
            after(resModel);
        }
        return resModel;
    }

    private OkHttpClient getOkHttpClient(RestActionReqData reqModel) {
        if (reqModel.getOkHttpClientBuilder() != null) {
            return reqModel.getOkHttpClientBuilder().build();
        }
        return restClientHandler.getClient();
    }

    private void buildHeader(RestClientUtils restClientUtils, RestActionReqData reqData) {
        if (reqData.headerValueMap != null) {
            reqData.headerValueMap.forEach(restClientUtils::setHeader);
        } else {
            if (cacheHeaderValueMap != null) {
                cacheHeaderValueMap.forEach(restClientUtils::setHeader);
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
                    if (!annotation.value().equals("")) {
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
     * @param reqModel Action request model
     * @return RequestBody
     */
    protected RequestBody buildRequestBody(Req reqModel) {
        if (!restAction.mediaType().equals("")) {
            MediaType mediaType = MediaType.parse(restAction.mediaType());
            if (mediaType == null) {
                throw new MediaTypeParseFailException("mediaType parse fail,please check mediaType");
            }
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
        }
        return null;
    }

    private boolean checkMockExist() {
        if (MOCK_PATH.equals("")) {
            return false;
        }
        return FileUtils.isExist(MOCK_PATH + this.getClass().getSimpleName() + ".json");
    }

    private void checkHttpCode(int code) {
        if (cacheSuccessHttpCodeList != null) {
            if (!cacheSuccessHttpCodeList.contains(code)) {
                throw new HttpCodeErrorException(code, "client code error : " + code);
            }
        }
    }

    private void processMockData(RestActionResData<Res> actionData) {
        try (Stream<String> stream = FileUtils.readFile(MOCK_PATH + this.getClass().getSimpleName() + ".json")) {
            String res = stream.collect(Collectors.joining());
            Optional<JsonNode> jsonNodeOptional = jacksonUtils.readTree(res);
            jsonNodeOptional.orElseThrow(Exception::new);
            JsonNode jsonNode = jsonNodeOptional.get();
            String sleep = jsonNode.get("sleep").asText();
            Thread.sleep(Long.parseLong(sleep));
            processResBody(actionData, jsonNode.get("data").toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new MockDataParseFailException("mock data format error");
        }
    }

    protected Res processResBody(RestActionResData<Res> actionData, byte[] resBody) {
        JsonNode resultNode = formatResponseBody(new String(resBody, StandardCharsets.UTF_8));
        if (getActionRestResModelClass() != null) {
            Optional<? extends RestActionResModel> actionRestResModelOptional = jacksonUtils.readValue(resultNode.toString(), getActionRestResModelClass());
            if (actionRestResModelOptional.isPresent() && actionRestResModelOptional.get().getCode() != null) {
                RestActionResModel restActionResModel = actionRestResModelOptional.get();
                throw new BreakActionLogicException(restActionResModel.getCode(), restActionResModel.getDesc(), processResModel(resultNode));
            } else {
                throw new ActionRestResModelFormatFailException("restActionResModel deserialize error");
            }
        }
        return processResModel(resultNode);
    }

    private Res processResModel(JsonNode resultNode) {
        if (!getResModelClass().equals(ActionResModel.class)) {
            Optional<Res> resModelOptional = jacksonUtils.readValue(processResponseBody(resultNode).toString(), getResModelClass());
            if (resModelOptional.isPresent()) {
                return resModelOptional.get();
            } else {
                throw new ActionResModelFormatErrorException("resModel format error");
            }
        }
        return null;
    }

    @PostConstruct
    private void init() {
        cacheSuccessHttpCodeList = buildCacheSuccessHttpCode();
        cacheHeaderValueMap = buildRequestCacheHeaderValue();
        restAction = this.getClass().getAnnotation(RestAction.class);
    }

}
