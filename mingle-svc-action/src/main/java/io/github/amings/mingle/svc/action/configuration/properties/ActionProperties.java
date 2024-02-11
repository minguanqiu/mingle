package io.github.amings.mingle.svc.action.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ming
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("mingle.svc.action")
public class ActionProperties {

    private String successCode = "0000";

    private String successDesc = "successful";

    private boolean autoBreak;

    private String msgType = "action";

}
