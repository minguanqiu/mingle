package io.github.minguanqiu.mingle.svc.data.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties} mapping data properties.
 *
 * @author Qiu Guan Ming
 */
@Getter
@Setter
public class SvcDataProperties {

  /**
   * Logging feature.
   *
   * @param logging the feature enable logging.
   * @return return ture or false decide enable status.
   */
  private boolean logging;

}
