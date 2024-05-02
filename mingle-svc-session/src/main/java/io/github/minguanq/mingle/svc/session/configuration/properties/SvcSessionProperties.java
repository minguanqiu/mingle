package io.github.minguanq.mingle.svc.session.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties} mapping session properties
 *
 * @author Ming
 */
@Getter
@Setter
public class SvcSessionProperties {

    private String header = "token";

}
