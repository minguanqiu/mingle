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
@ConfigurationProperties("mingle.svc")
public class SvcProperties {

    private String rootPath = "/svc";

    private String msgType = "svc";

    private String successCode = "0";

    private String successMsg = "successful";

}
