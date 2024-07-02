package io.github.minguanqiu.mingle.svc.action.rest;

import io.github.minguanqiu.mingle.svc.action.rest.configuration.properties.RestActionProperties;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Response;
import org.springframework.stereotype.Service;

/**
 * @author Qiu Guan Ming
 */
@Service
public class SimpleAction2 extends AbstractSimpleAction<SimpleActionReq, SimpleActionRes> {

  public SimpleAction2(RestActionProperties actionProperties) {
    super(actionProperties);
  }

  @Override
  protected List<String> buildRestPath(SimpleActionReq request) {
    return List.of();
  }

  @Override
  protected Map<String, Object> buildActionInfoValue(SimpleActionReq request,
      SimpleActionRes resBModel, Response response) {
    HashMap<String, Object> stringObjectHashMap = new HashMap<>();
    stringObjectHashMap.put("httpCode", response.code());
    return stringObjectHashMap;
  }
}
