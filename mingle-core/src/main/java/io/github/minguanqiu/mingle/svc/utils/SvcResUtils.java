package io.github.minguanqiu.mingle.svc.utils;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.minguanqiu.mingle.svc.SvcResponseBody;
import io.github.minguanqiu.mingle.svc.handler.SvcResponseHandler;
import org.springframework.objenesis.SpringObjenesis;

/**
 * Utils for service response process.
 *
 * @author Qiu Guan Ming
 */
public class SvcResUtils {

  private final SpringObjenesis springObjenesis = new SpringObjenesis();
  private final SvcResponseHandler svcResponseHandler;
  private final JacksonUtils jacksonUtils;

  /**
   * Create a new SvcResUtils instance.
   *
   * @param svcResponseHandler the service response handler.
   * @param jacksonUtils       the jackson utils.
   */
  public SvcResUtils(SvcResponseHandler svcResponseHandler, JacksonUtils jacksonUtils) {
    this.svcResponseHandler = svcResponseHandler;
    this.jacksonUtils = jacksonUtils;
  }

  /**
   * Build service response header instance.
   *
   * @param code the service response code.
   * @param msg  the service response message.
   * @return return the service response handler.
   */
  public SvcResponseHandler build(String code, String msg) {
    return build(code, msg, new SvcResponseBody());
  }

  /**
   * Build service response header instance.
   *
   * @param code            the service response code.
   * @param msg             the service response message.
   * @param svcResponseBody the service response body.
   * @return return the service response handler.
   */
  public SvcResponseHandler build(String code, String msg, SvcResponseBody svcResponseBody) {
    return build(code, msg, svcResponseBody, jacksonUtils);
  }

  /**
   * Build service response header instance.
   *
   * @param code            the service response code.
   * @param msg             the service response message.
   * @param svcResponseBody the service response body.
   * @param jacksonUtils    the jackson utils.
   * @return return the service response handler.
   */
  public SvcResponseHandler build(String code, String msg, SvcResponseBody svcResponseBody,
      JacksonUtils jacksonUtils) {
    return build(code, msg, jacksonUtils.readTree(svcResponseBody).orElse(null));
  }

  /**
   * Build service response header instance.
   *
   * @param code     the service response code.
   * @param msg      the service response message.
   * @param jsonNode the json node.
   * @return return the service response handler.
   */
  public SvcResponseHandler build(String code, String msg, JsonNode jsonNode) {
    SvcResponseHandler svcResponseHandlerImpl = springObjenesis.newInstance(
        svcResponseHandler.getClass(), true);
    svcResponseHandlerImpl.setCode(code);
    svcResponseHandlerImpl.setMsg(msg);
    svcResponseHandlerImpl.setResponseBody(jsonNode);
    return svcResponseHandlerImpl;
  }

}
