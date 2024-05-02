package io.github.minguanq.mingle.svc.action.rest;

import io.github.minguanq.mingle.svc.action.ActionRequest;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;

/**
 * {@inheritDoc}
 * Action request for rest type
 *
 * @author Ming
 */

@Getter
@Setter
public class RestActionRequest extends ActionRequest {

    private OkHttpClient.Builder okHttpClientBuilder;

}
