package io.github.minguanq.mingle.svc.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * {@link ConfigurationProperties} mapping service properties
 *
 * @author Ming
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("mingle.svc.properties")
public class SvcProperties {

    private String msg_type = "svc";

    private String code = "0";

    private String msg = "successful";

    private boolean logging = true;

    private boolean body_process;

    private String[] ip_secure = {};

}
