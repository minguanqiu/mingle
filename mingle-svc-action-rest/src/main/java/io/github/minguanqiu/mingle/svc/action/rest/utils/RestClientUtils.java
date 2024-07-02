package io.github.minguanqiu.mingle.svc.action.rest.utils;

import io.github.minguanqiu.mingle.svc.action.rest.enums.HttpMethod;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Qiu Guan Ming
 */

@Slf4j
public class RestClientUtils {

  private final OkHttpClient okHttpClient;
  private final Map<String, String> headerValuesMap = new HashMap<>();

  @Getter
  private HttpUrl httpUrl;
  @Getter
  @Setter
  private HttpMethod httpMethod;
  private RequestBody requestBody;
  @Setter
  private HashMap<String, String> queryParameterMap;
  @Getter
  @Setter
  private String result;

  public RestClientUtils() {
    okHttpClient = new OkHttpClient();
  }

  public RestClientUtils(OkHttpClient okHttpClient) {
    this.okHttpClient = okHttpClient;
  }

  public RestClientUtils(int connectTimeOut, int readTimeOut) {
    okHttpClient = new OkHttpClient().newBuilder()
        .connectTimeout(Duration.ofSeconds(connectTimeOut))
        .readTimeout(Duration.ofSeconds(readTimeOut)).build();
  }

  public RestClientUtils setHeader(String key, String value) {
    headerValuesMap.put(key, value);
    return this;
  }

  public void setRequestBody(RequestBody requestBody) {
    this.requestBody = requestBody;
  }

  public void buildHttpUrl(String uri) {
    Objects.requireNonNull(uri, " url can't null");
    HttpUrl httpUrl = HttpUrl.parse(uri);
    Objects.requireNonNull(httpUrl, uri + " url parse fail");
    HttpUrl.Builder builder = httpUrl.newBuilder();
    if (queryParameterMap != null) {
      queryParameterMap.forEach(builder::addQueryParameter);
    }
    this.httpUrl = builder.build();
    log.info("【Client URI】: " + this.httpUrl);
  }

  public Response call() throws IOException {
    Request.Builder builder = new Request.Builder().url(httpUrl)
        .method(httpMethod.name(), requestBody);
    headerValuesMap.forEach(builder::header);
    try {
      return okHttpClient.newCall(builder.build()).execute();
    } catch (IOException e) {
      throw new IOException(e);
    }
  }

}
