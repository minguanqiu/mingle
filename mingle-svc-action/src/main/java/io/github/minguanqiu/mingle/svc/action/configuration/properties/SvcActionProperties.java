package io.github.minguanqiu.mingle.svc.action.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties} mapping action properties
 *
 * @author Ming
 */
@Getter
@Setter
public class SvcActionProperties {

    private String code = "0000";

    private String msg = "successful";

    private boolean auto_break;

    private String msg_type = "action";

}
