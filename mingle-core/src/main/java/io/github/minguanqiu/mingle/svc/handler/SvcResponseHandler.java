package io.github.minguanqiu.mingle.svc.handler;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Handler for service response body format
 *
 * @author Qiu Guan Ming
 */

public interface SvcResponseHandler {

  void setResponseBody(JsonNode jsonNode);

  JsonNode getResponseBody();

  void setCode(String code);

  String getCode();

  void setMsg(String msg);

  String getMsg();

}
