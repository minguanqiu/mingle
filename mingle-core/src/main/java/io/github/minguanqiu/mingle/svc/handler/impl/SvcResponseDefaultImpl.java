package io.github.minguanqiu.mingle.svc.handler.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.minguanqiu.mingle.svc.handler.SvcResponseHandler;

/**
 * {@inheritDoc} Default impl for {@link SvcResponseHandler}
 *
 * @author Qiu Guan Ming
 */
@JsonPropertyOrder({"code", "msg", "body"})
public class SvcResponseDefaultImpl implements SvcResponseHandler {

  private JsonNode body;

  private String code;

  private String msg;

  @Override
  public void setResponseBody(JsonNode jsonNode) {
    this.body = jsonNode;
  }

  @JsonProperty("body")
  @Override
  public JsonNode getResponseBody() {
    return body;
  }

  @Override
  public void setCode(String code) {
    this.code = code;
  }

  @JsonProperty("code")
  @Override
  public String getCode() {
    return code;
  }

  @Override
  public void setMsg(String msg) {
    this.msg = msg;
  }

  @JsonProperty("msg")
  @Override
  public String getMsg() {
    return msg;
  }
}
