package io.github.minguanqiu.mingle.svc.handler;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Handler for service response body format.
 *
 * @author Qiu Guan Ming
 */

public interface SvcResponseHandler {

  /**
   * Set service response body.
   *
   * @param jsonNode the jackson node.
   */
  void setResponseBody(JsonNode jsonNode);

  /**
   * Get service response body.
   *
   * @return return the jackson node.
   */
  JsonNode getResponseBody();

  /**
   * Set service response code.
   *
   * @param code the response code.
   */
  void setCode(String code);

  /**
   * Get service response code.
   *
   * @return return the service response code.
   */
  String getCode();

  /**
   * Set service response message.
   *
   * @param msg the response message.
   */
  void setMsg(String msg);

  /**
   * Get service response message.
   *
   * @return return the service response message.
   */
  String getMsg();

}
