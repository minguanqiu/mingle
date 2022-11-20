package io.github.amings.mingle.svc.data;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Doa auto configuration
 *
 * @author Ming
 */

@AutoConfiguration
@ComponentScan(basePackageClasses = DataAutoConfiguration.class)
public class DataAutoConfiguration {
}
