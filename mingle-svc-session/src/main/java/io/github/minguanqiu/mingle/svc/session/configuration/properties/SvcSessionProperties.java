package io.github.minguanqiu.mingle.svc.session.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties} mapping session properties.
 *
 * @author Qiu Guan Ming
 */
@Getter
@Setter
public class SvcSessionProperties {

  /**
   * The session token header name.
   *
   * @param header the session token header name.
   * @return return token header name.
   */
  private String header = "token";

}
