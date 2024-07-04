package io.github.minguanqiu.mingle.svc.action.rest;

import io.github.minguanqiu.mingle.svc.action.ActionRequest;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;

/**
 * Action request for rest feature.
 *
 * @author Qiu Guan Ming
 */

@Getter
@Setter
public class RestActionRequest extends ActionRequest {

  /**
   * Custom okhttp builder.
   *
   * @param okHttpClientBuilder the custom okhttp builder.
   * @return return the custom okhttp builder.
   */
  private OkHttpClient.Builder okHttpClientBuilder;

}
