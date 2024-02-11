package io.github.amings.mingle.svc.action.rest;

import io.github.amings.mingle.svc.action.ActionReqModel;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;

import java.util.Map;

/**
 * @author Ming
 */

@Getter
@Setter
public class RestActionReqModel extends ActionReqModel {

    private OkHttpClient.Builder okHttpClientBuilder;

    private Map<String,String> headerValueMap;

}
