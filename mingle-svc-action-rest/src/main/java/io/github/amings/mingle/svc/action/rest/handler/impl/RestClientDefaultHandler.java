package io.github.amings.mingle.svc.action.rest.handler.impl;

import io.github.amings.mingle.svc.action.rest.component.RestClientComponent;
import io.github.amings.mingle.svc.action.rest.handler.RestClientHandler;
import jakarta.annotation.PostConstruct;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Ming
 */

public class RestClientDefaultHandler implements RestClientHandler {

    @Autowired
    RestClientComponent restClientComponent;

    @Value("${mingle.svc.action.rest.client.connectTimeOut:3000}")
    private int connectTimeOut;

    @Value("${mingle.svc.action.rest.client.readTimeOut:70000}")
    private int readTimeOut;

    @Value("${mingle.svc.action.rest.client.ignoreSSL:false}")
    private boolean ignoreSSL;
    private OkHttpClient okHttpClient;

    @Override
    public OkHttpClient getClient() {
        return okHttpClient;
    }

    @PostConstruct
    private void init() {
        okHttpClient = restClientComponent.buildClient(connectTimeOut, readTimeOut, ignoreSSL).build();
    }

}
