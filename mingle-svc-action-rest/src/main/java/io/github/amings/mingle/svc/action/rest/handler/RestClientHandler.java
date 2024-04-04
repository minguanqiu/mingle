package io.github.amings.mingle.svc.action.rest.handler;

import okhttp3.OkHttpClient;

/**
 * Handler for okhttp client
 *
 * @author Ming
 */

public interface RestClientHandler {

    OkHttpClient getClient();

}
