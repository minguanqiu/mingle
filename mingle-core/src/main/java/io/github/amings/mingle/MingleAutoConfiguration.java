package io.github.amings.mingle;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * {@link AutoConfiguration} for mingle service
 *
 * @author Ming
 */

@AutoConfiguration
@ComponentScan(basePackageClasses = MingleAutoConfiguration.class)
public class MingleAutoConfiguration {
}
