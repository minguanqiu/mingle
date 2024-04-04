package io.github.amings.mingle.svc.session.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * {@link ConfigurationProperties} mapping session properties
 *
 * @author Ming
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("mingle.svc.session")
public class SessionProperties {

    private String header = "token";

}
