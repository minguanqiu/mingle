package io.github.minguanq.mingle.svc.action.rest.handler.impl;

import io.github.minguanq.mingle.svc.action.rest.handler.RestClientHandler;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * {@inheritDoc}
 * Default impl for {@link RestClientHandler}
 *
 * @author Ming
 */

public class RestClientDefaultHandler implements RestClientHandler {

    private final OkHttpClient okHttpClient;

    public RestClientDefaultHandler() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.connectTimeout(5000, TimeUnit.MILLISECONDS)
                .readTimeout(70000,TimeUnit.MILLISECONDS);
        this.okHttpClient = builder.build();
    }

    @Override
    public OkHttpClient getClient() {
        return okHttpClient;
    }

}
