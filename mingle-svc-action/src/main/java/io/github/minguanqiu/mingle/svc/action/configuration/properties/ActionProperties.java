package io.github.minguanqiu.mingle.svc.action.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties} mapping action properties.
 *
 * @author Qiu Guan Ming
 */
@Getter
@Setter
public class ActionProperties {

  /**
   * Action response code.
   *
   * @param code the action response code.
   * @return return the action response code.
   */
  private String code = "0000";

  /**
   * Action response message.
   *
   * @param msg the action response message.
   * @return return the action response message.
   */
  private String msg = "successful";

  /**
   * Automatic break action logic.
   *
   * @param autoBreak whether automatic break action logic.
   * @return return the ture or false to automatic break action.
   */
  private boolean autoBreak;

  /**
   * Action message type.
   *
   * @param msgType the type for action message.
   * @return return the action message type.
   */
  private String msgType = "action";

}
