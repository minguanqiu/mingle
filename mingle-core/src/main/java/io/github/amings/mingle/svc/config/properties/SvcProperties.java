package io.github.amings.mingle.svc.config.properties;

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
@ConfigurationProperties("mingle.svc")
public class SvcProperties {

    private String rootPath = "/svc";

    private String msgType = "svc";

    private String successCode = "0";

    private String successDesc = "successful";

}
