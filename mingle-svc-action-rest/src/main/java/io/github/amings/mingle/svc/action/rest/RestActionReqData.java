package io.github.amings.mingle.svc.action.rest;

import io.github.amings.mingle.svc.action.ActionReqData;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;

import java.util.Map;

/**
 * Action request model for rest client
 *
 * @author Ming
 */

@Getter
@Setter
public class RestActionReqData extends ActionReqData {

    OkHttpClient.Builder okHttpClientBuilder;

    Map<String,String> headerValueMap;

}
