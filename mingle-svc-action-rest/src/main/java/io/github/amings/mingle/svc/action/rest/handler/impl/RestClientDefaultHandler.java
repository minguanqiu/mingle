package io.github.amings.mingle.svc.action.rest.handler.impl;

import io.github.amings.mingle.svc.action.rest.configuration.properties.RestClientProperties;
import io.github.amings.mingle.svc.action.rest.handler.RestClientHandler;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * @author Ming
 */

public class RestClientDefaultHandler implements RestClientHandler {

    private final OkHttpClient okHttpClient;

    public RestClientDefaultHandler(RestClientProperties restClientProperties) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.connectTimeout(restClientProperties.getConnectTimeOut(), TimeUnit.MILLISECONDS)
                .readTimeout(restClientProperties.getReadTimeOut(),TimeUnit.MILLISECONDS);
        this.okHttpClient = builder.build();
    }

    @Override
    public OkHttpClient getClient() {
        return okHttpClient;
    }

}
