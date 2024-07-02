package io.github.minguanqiu.mingle.svc.session.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties} mapping session properties
 *
 * @author Qiu Guan Ming
 */
@Getter
@Setter
public class SvcSessionProperties {

  private String header = "token";

}
