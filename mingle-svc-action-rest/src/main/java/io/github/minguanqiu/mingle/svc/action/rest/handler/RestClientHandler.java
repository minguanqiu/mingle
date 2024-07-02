package io.github.minguanqiu.mingle.svc.action.rest.handler;

import okhttp3.OkHttpClient;

/**
 * Handler for okhttp client
 *
 * @author Qiu Guan Ming
 */

public interface RestClientHandler {

  OkHttpClient getClient();

}
