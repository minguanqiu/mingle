package io.github.minguanqiu.mingle.svc.action.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties} mapping action properties
 *
 * @author Qiu Guan Ming
 */
@Getter
@Setter
public class ActionProperties {

  private String code = "0000";

  private String msg = "successful";

  private boolean autoBreak;

  private String msgType = "action";

}
