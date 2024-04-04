package io.github.minguanq.mingle.svc;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * {@link AutoConfiguration} for mingle service
 *
 * @author Ming
 */

@AutoConfiguration
@ComponentScan(basePackageClasses = SvcAutoConfiguration.class)
public class SvcAutoConfiguration {
}
