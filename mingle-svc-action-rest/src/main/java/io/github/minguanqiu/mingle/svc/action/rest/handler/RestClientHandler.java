package io.github.minguanqiu.mingle.svc.action.rest.handler;

import okhttp3.OkHttpClient;

/**
 * Handler for okhttp client.
 *
 * @author Qiu Guan Ming
 */

public interface RestClientHandler {

  /**
   * Build okhttp client.
   *
   * @return return the new okhttp client object.
   */
  OkHttpClient getClient();

}
