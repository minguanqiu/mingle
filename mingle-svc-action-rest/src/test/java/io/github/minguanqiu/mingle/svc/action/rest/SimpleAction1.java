package io.github.minguanqiu.mingle.svc.action.rest;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.minguanqiu.mingle.svc.action.rest.configuration.properties.RestActionProperties;
import io.github.minguanqiu.mingle.svc.action.rest.exception.ActionResponseBodyDeserializeErrorException;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author Qiu Guan Ming
 */
@Service
public class SimpleAction1 extends AbstractSimpleAction<SimpleActionReq, SimpleActionRes> {

  public SimpleAction1(RestActionProperties actionProperties) {
    super(actionProperties);
  }

  @Override
  protected List<String> buildRestPath(SimpleActionReq request) {
    return List.of("SimpleSvc");
  }

  @Override
  protected SimpleActionRes deserializeResponseBody(JsonNode resultNode) {
    return jacksonUtils.readValue(resultNode.get("body").toString(), resClass)
        .orElseThrow(ActionResponseBodyDeserializeErrorException::new);
  }

}
