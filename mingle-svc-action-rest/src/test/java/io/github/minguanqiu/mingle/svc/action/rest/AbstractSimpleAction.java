package io.github.minguanqiu.mingle.svc.action.rest;

import io.github.minguanqiu.mingle.svc.action.ActionResponseBody;
import io.github.minguanqiu.mingle.svc.action.rest.configuration.properties.RestActionProperties;
import io.github.minguanqiu.mingle.svc.action.rest.enums.HttpMethod;

/**
 * @author Qiu Guan Ming
 */
public abstract class AbstractSimpleAction<Req extends RestActionRequest, ResB extends ActionResponseBody> extends
    AbstractRestAction<Req, ResB> {

  public AbstractSimpleAction(RestActionProperties actionProperties) {
    super(actionProperties);
  }

  @Override
  public HttpMethod getHttpMethod() {
    return HttpMethod.POST;
  }

  @Override
  public String getServerName() {
    return "Simple";
  }

  @Override
  public String getType() {
    return "Simple";
  }

}
