package io.github.minguanqiu.mingle.svc.action.rest.handler.impl;

import io.github.minguanqiu.mingle.svc.action.rest.handler.RestClientHandler;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

/**
 * Default impl for {@link RestClientHandler}.
 *
 * @author Qiu Guan Ming
 */

public class RestClientDefaultHandler implements RestClientHandler {

  private final OkHttpClient okHttpClient;

  /**
   * Create a new RestClientDefaultHandler instance.
   */
  public RestClientDefaultHandler() {
    OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
    builder.connectTimeout(5000, TimeUnit.MILLISECONDS)
        .readTimeout(70000, TimeUnit.MILLISECONDS);
    this.okHttpClient = builder.build();
  }

  @Override
  public OkHttpClient getClient() {
    return okHttpClient;
  }

}
