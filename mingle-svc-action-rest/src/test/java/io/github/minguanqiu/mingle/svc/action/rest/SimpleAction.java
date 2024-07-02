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
public class SimpleAction extends AbstractSimpleAction<SimpleActionReq, SimpleActionRes> {

  public SimpleAction(RestActionProperties actionProperties) {
    super(actionProperties);
  }

  @Override
  protected List<String> buildRestPath(SimpleActionReq request) {
    return List.of("SimpleSvc");
  }

  @Override
  protected RestActionResponseBodyHeader buildResponseBodyHeader(JsonNode resultNode) {
    RestActionResponseBodyHeader restActionResponseBodyHeader = new RestActionResponseBodyHeader();
    restActionResponseBodyHeader.setSuccessCode("0");
    restActionResponseBodyHeader.setCode(resultNode.get("code").asText());
    restActionResponseBodyHeader.setMsg(resultNode.get("msg").asText());
    return restActionResponseBodyHeader;
  }

  @Override
  protected SimpleActionRes deserializeResponseBody(JsonNode resultNode) {
    return jacksonUtils.readValue(resultNode.get("body").toString(), resClass)
        .orElseThrow(ActionResponseBodyDeserializeErrorException::new);
  }
}
