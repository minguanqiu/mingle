package io.github.amings.mingle.svc.session;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * {@link AutoConfiguration} for session module
 *
 * @author Ming
 */

@AutoConfiguration
@ComponentScan(basePackageClasses = SessionAutoConfiguration.class)
public class SessionAutoConfiguration {
}
