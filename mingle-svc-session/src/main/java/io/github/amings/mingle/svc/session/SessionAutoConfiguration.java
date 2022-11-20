package io.github.amings.mingle.svc.session;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Session auto configuration
 *
 * @author Ming
 */

@AutoConfiguration
@ComponentScan(basePackageClasses = SessionAutoConfiguration.class)
public class SessionAutoConfiguration {
}
