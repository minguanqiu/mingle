package io.github.amings.mingle.svc.action.rest.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * {@link ConfigurationProperties} mapping action rest client properties
 *
 * @author Ming
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("mingle.svc.action.rest.client")
public class RestClientProperties {

    private long connectTimeOut = 3000;

    private long readTimeOut = 70000;

}
