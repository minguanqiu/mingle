package io.github.minguanqiu.mingle.svc.action;

import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * This class provide action response result successful or fail.
 *
 * @author Qiu Guan Ming
 */

@Getter
@Setter(AccessLevel.PACKAGE)
public class ActionResponse<ResB extends ActionResponseBody> {

  /**
   * Action successful or fail.
   *
   * @return return the ture or false to decide result.
   */
  private boolean success;

  /**
   * Action response code.
   *
   * @return return the action response code.
   */
  private String code;

  /**
   * Action response message.
   *
   * @return return the action response message.
   */
  private String msg;

  /**
   * Action process tmp map.
   *
   * @return return the action process tmp map.
   */
  private Map<String, Object> values;

  /**
   * Action response body.
   *
   * @return return the action response body.
   */
  private ResB responseBody;

  /**
   * Action response message type.
   *
   * @return return the action message type.
   */
  private String msgType;

  /**
   * Get action response body.
   *
   * @return return the optional of response body.
   */
  public Optional<ResB> getResponseBody() {
    return Optional.ofNullable(responseBody);
  }
}
