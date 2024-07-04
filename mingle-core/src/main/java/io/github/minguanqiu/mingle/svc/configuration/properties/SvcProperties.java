package io.github.minguanqiu.mingle.svc.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties} mapping service properties.
 *
 * @author Qiu Guan Ming
 */
@Getter
@Setter
public class SvcProperties {

  /**
   * Service message type.
   *
   * @param msgType the message type.
   * @return return a message type.
   */
  private String msgType = "svc";

  /**
   * Service successful code in response body.
   *
   * @param code the response code.
   * @return return a response code.
   */
  private String code = "0000";

  /**
   * Service successful message in response body.
   *
   * @param msg the response message.
   * @return return a response message.
   */
  private String msg = "successful";

  /**
   * Service feature.
   *
   * @param feature the service feature.
   * @return return a service feature.
   */
  private SvcFeature feature = new SvcFeature();

}
