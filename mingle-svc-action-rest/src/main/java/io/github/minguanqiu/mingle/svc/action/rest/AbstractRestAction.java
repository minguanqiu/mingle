package io.github.minguanqiu.mingle.svc.action.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.reflect.TypeToken;
import io.github.minguanqiu.mingle.svc.action.AbstractAction;
import io.github.minguanqiu.mingle.svc.action.ActionInfo;
import io.github.minguanqiu.mingle.svc.action.ActionResponseBody;
import io.github.minguanqiu.mingle.svc.action.rest.annotation.QueryParameter;
import io.github.minguanqiu.mingle.svc.action.rest.configuration.properties.RestActionProperties;
import io.github.minguanqiu.mingle.svc.action.rest.enums.HttpMethod;
import io.github.minguanqiu.mingle.svc.action.rest.exception.ActionRequestSerializeErrorException;
import io.github.minguanqiu.mingle.svc.action.rest.exception.ActionResponseBodyDeserializeErrorException;
import io.github.minguanqiu.mingle.svc.action.rest.exception.ClientErrorException;
import io.github.minguanqiu.mingle.svc.action.rest.exception.HttpCodeErrorException;
import io.github.minguanqiu.mingle.svc.action.rest.exception.MockErrorException;
import io.github.minguanqiu.mingle.svc.action.rest.exception.ResponseBodyNotJsonFormatFailException;
import io.github.minguanqiu.mingle.svc.action.rest.exception.ServerNotExistException;
import io.github.minguanqiu.mingle.svc.action.rest.handler.RestClientHandler;
import io.github.minguanqiu.mingle.svc.action.rest.handler.impl.RestClientDefaultHandler;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * <p>Action for restful feature.
 * <pre>
 * Feature :
 * Request serialize and response deserialize using jackson.
 * Target server response unite header process to decide action success or fail.
 * </pre>
 *
 * @param <R1> action request.
 * @param <R2> action response body.
 * @author Qiu Guan Ming
 */

public abstract class AbstractRestAction<R1 extends RestActionRequest, R2 extends ActionResponseBody>
    extends AbstractAction<R1, R2> {

  /**
   * Properties for rest action.
   */
  protected final RestActionProperties restActionProperties;
  /**
   * Handler for okhttp client.
   */
  public RestClientHandler restClientHandler = new RestClientDefaultHandler();
  /**
   * Utils for jackson process.
   */
  public JacksonUtils jacksonUtils = new JacksonUtils(new ObjectMapper());
  private Map<String, String> commonHeaderValueMap;
  private Set<Integer> successHttpCodeSet;
  /**
   * Response body class.
   */
  protected final Class<R2> resClass;

  /**
   * Create a new AbstractRestAction implementations instance.
   *
   * @param actionProperties the properties for rest action.
   */
  @SuppressWarnings("unchecked")
  public AbstractRestAction(RestActionProperties actionProperties) {
    super(actionProperties);
    this.restActionProperties = actionProperties;
    resClass = (Class<R2>) new TypeToken<R2>(getClass()) {
    }.getRawType();
    init();
  }


  @Override
  protected final R2 processLogic(R1 request, ActionInfo actionInfo) {
    before(request);
    ResponseData responseData =
        !restActionProperties.getRest().getMock().containsKey(getMockName()) ? processCall(
            request) : processMockCall();
    R2 responseBody = buildResponseBody(actionInfo, responseData.responseBytes());
    Map<String, Object> actionInfoValue = buildActionInfoValue(request, responseBody,
        responseData.response());
    if (actionInfoValue != null) {
      actionInfo.getValues().putAll(actionInfoValue);
    }
    Response response = responseData.response();
    checkHttpCode(response.code());
    if (actionProperties.getCode().equals(actionInfo.getCode()) && responseBody != null) {
      after(responseBody);
    }
    response.close();
    return responseBody;
  }

  /**
   * Process okhttp call.
   *
   * @param request the action request.
   * @return return the response data.
   */
  private ResponseData processCall(R1 request) {
    try (Response response = getOkHttpClient(request).newCall(buildHttpRequest(request))
        .execute()) {
      if (response.cacheResponse() != null) {
        return new ResponseData(response.cacheResponse(), response.body().bytes());
      }
      return new ResponseData(response, response.body().bytes());
    } catch (IOException e) {
      throw new ClientErrorException("client error : " + e.getMessage(), e);
    }
  }

  /**
   * Process mock call.
   *
   * @return return the response data.
   */
  private ResponseData processMockCall() {
    RestActionProperties.RestProperties.MockProperties mockProperties =
        restActionProperties.getRest().getMock().get(getMockName());
    Response.Builder responseBuilder = new Response.Builder();
    responseBuilder.code(mockProperties.getCode());
    mockProperties.getHeader().forEach(responseBuilder::addHeader);
    responseBuilder.body(ResponseBody.create(mockProperties.getResponseBody().getMediaType(),
        MediaType.parse(mockProperties.getResponseBody().getContent())));
    responseBuilder.message(mockProperties.getMessage());
    try (Response response = responseBuilder.build()) {
      Thread.sleep(mockProperties.getDelay());
      return new ResponseData(response, response.body().bytes());
    } catch (Exception e) {
      throw new MockErrorException(e);
    }
  }

  /**
   * Build action information tmp map.
   *
   * @param request      the action request.
   * @param responseBody the action response body.
   * @param response     the action response.
   * @return return action information tmp map.
   */
  protected Map<String, Object> buildActionInfoValue(R1 request, R2 responseBody,
      Response response) {
    return null;
  }

  /**
   * Build okhttp request info.
   *
   * @param request the action request.
   * @return return okhttp request object.
   */
  protected Request buildHttpRequest(R1 request) {
    Request.Builder requestBuilder = new Request.Builder();
    if (commonHeaderValueMap != null) {
      commonHeaderValueMap.forEach(requestBuilder::addHeader);
    }
    requestBuilder.url(buildUrl(request));
    if (getHttpMethod().equals(HttpMethod.GET)) {
      requestBuilder.get();
    } else {
      requestBuilder.method(getHttpMethod().name(), buildRequestBody(request));
    }
    return requestBuilder.build();
  }

  /**
   * Setting action http method.
   *
   * @return the action http method.
   */
  public abstract HttpMethod getHttpMethod();

  /**
   * Setting action http suffix path.
   *
   * @param request the action request.
   * @return the action http suffix path.
   */
  protected abstract List<String> buildRestPath(R1 request);

  /**
   * Get action defined server name to mapping properties.
   *
   * @return return the action defined server name.
   */
  public abstract String getServerName();

  /**
   * Get mock name to mapping properties.
   *
   * @return return the mock name.
   */
  public String getMockName() {
    return this.getClass().getSimpleName();
  }

  /**
   * Build action request header for cache.
   *
   * @return return the header map.
   */
  protected Map<String, String> buildCommonHeaderValue() {
    return null;
  }

  /**
   * Build action success http code for cache,if not contains code,this action will set error code
   * and not success.
   *
   * @return return the set of http code.
   */
  protected Set<Integer> buildSuccessHttpCodeSet() {
    return Set.of(200);
  }

  /**
   * Http response body format specification to json object.
   *
   * @param responseBody the http response body.
   * @return return the json node.
   */
  protected JsonNode formatRawResponseBody(String responseBody) {
    return jacksonUtils.readTree(responseBody)
        .orElseThrow(ResponseBodyNotJsonFormatFailException::new);
  }


  /**
   * Pre-processing action request.
   *
   * @param request the action request.
   */
  protected void before(R1 request) {
  }

  /**
   * Post-processing action response body.
   *
   * @param responseBody the action response body.
   */
  protected void after(R2 responseBody) {
  }

  /**
   * Build response body header from API.
   *
   * @param resultNode the http response body json node.
   * @return return the response body header from API.
   */
  protected RestActionResponseBodyHeader buildResponseBodyHeader(JsonNode resultNode) {
    return null;
  }

  /**
   * Get okhttp client and check custom.
   *
   * @param request the rest action request.
   * @return return the okhttp client object.
   */
  private OkHttpClient getOkHttpClient(RestActionRequest request) {
    if (request.getOkHttpClientBuilder() != null) {
      return request.getOkHttpClientBuilder().build();
    }
    return restClientHandler.getClient();
  }

  /**
   * Build okhttp request url.
   *
   * @param request the action request.
   * @return return http url object.
   */
  protected HttpUrl buildUrl(R1 request) {
    if (!restActionProperties.getRest().getServer().containsKey(getServerName())) {
      throw new ServerNotExistException("Server not exist with name: " + getServerName());
    }
    RestActionProperties.RestProperties.ServerProperties serverProperties =
        restActionProperties.getRest().getServer().get(getServerName());
    List<String> restPaths = buildRestPath(request);
    HttpUrl.Builder builder = new HttpUrl.Builder().scheme(serverProperties.getScheme())
        .host(serverProperties.getHost()).port(serverProperties.getPort());
    for (String pathSegment : serverProperties.getPathSegments()) {
      builder.addPathSegments(pathSegment);
    }
    if (restPaths != null) {
      restPaths.forEach(builder::addPathSegment);
    }
    HashMap<String, String> pathVariable = buildQueryParameter(request);
    if (pathVariable != null) {
      pathVariable.forEach(builder::addQueryParameter);
    }
    return builder.build();
  }

  /**
   * Build request url query parameter.
   *
   * @param request the action request.
   * @return return the query parameter map.
   */
  protected HashMap<String, String> buildQueryParameter(R1 request) {
    HashMap<String, String> stringHashMap = null;
    for (Field field : request.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      QueryParameter annotation = field.getAnnotation(QueryParameter.class);
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
   * Build request body.
   *
   * @param request the action request.
   * @return return okhttp request body.
   */
  protected RequestBody buildRequestBody(R1 request) {
    Optional<JsonNode> jsonNodeOptional = jacksonUtils.readTree(request);
    jsonNodeOptional.orElseThrow(ActionRequestSerializeErrorException::new);
    ObjectNode objectNode = (ObjectNode) jsonNodeOptional.get();
    objectNode.remove("autoBreak");
    objectNode.remove("okHttpClientBuilder");
    return RequestBody.create(jsonNodeOptional.get().toString().getBytes(StandardCharsets.UTF_8),
        MediaType.parse("application/json"));
  }

  /**
   * Check http response code whether contains http code set, if ture will throw
   * {@link HttpCodeErrorException}.
   *
   * @param code the http response code.
   */
  private void checkHttpCode(int code) {
    if (successHttpCodeSet != null) {
      if (!successHttpCodeSet.contains(code)) {
        throw new HttpCodeErrorException(code, "client code error : " + code);
      }
    }
  }

  /**
   * Deserialize response body from http response raw body and check response body header success or
   * fail.
   *
   * @param actionInfo the action information.
   * @param resBody    the response body of byte.
   * @return return the response body object after deserialize.
   */
  private R2 buildResponseBody(ActionInfo actionInfo, byte[] resBody) {
    JsonNode responseBodyNode = formatRawResponseBody(new String(resBody, StandardCharsets.UTF_8));
    RestActionResponseBodyHeader restActionResponseBodyHeader = buildResponseBodyHeader(
        responseBodyNode);
    if (restActionResponseBodyHeader != null && !restActionResponseBodyHeader.getSuccessCode()
        .equals(restActionResponseBodyHeader.getCode())) {
      actionInfo.setCode(restActionResponseBodyHeader.getCode());
      actionInfo.setMsg(restActionResponseBodyHeader.getMsg());
    }
    return deserializeResponseBody(responseBodyNode);
  }

  /**
   * Deserialize response body from http response raw body.
   *
   * @param resultNode the response body json node.
   * @return return the response body object after deserialize.
   */
  protected R2 deserializeResponseBody(JsonNode resultNode) {
    return jacksonUtils.readValue(resultNode.toString(), resClass)
        .orElseThrow(ActionResponseBodyDeserializeErrorException::new);
  }

  private void init() {
    successHttpCodeSet = buildSuccessHttpCodeSet();
    commonHeaderValueMap = buildCommonHeaderValue();
  }

  /**
   * Setting custom rest client handler.
   *
   * @param restClientHandler the rest client handler.
   */
  @Autowired(required = false)
  public void setRestClientHandler(
      RestClientHandler restClientHandler) {
    this.restClientHandler = restClientHandler;
  }

  /**
   * Setting custom jackson utils.
   *
   * @param jacksonUtils the jackson utils.
   */
  @Autowired(required = false)
  public void setJacksonUtils(
      @Qualifier("restActionJacksonUtils") JacksonUtils jacksonUtils) {
    this.jacksonUtils = jacksonUtils;
  }

  /**
   * Http response data.
   *
   * @param response      the okhttp response object.
   * @param responseBytes the okhttp response body.
   */
  private record ResponseData(Response response, byte[] responseBytes) {

  }

}
