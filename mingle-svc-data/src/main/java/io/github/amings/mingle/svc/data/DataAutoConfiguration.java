package io.github.amings.mingle.svc.data;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * {@link AutoConfiguration} for data module
 *
 * @author Ming
 */

@AutoConfiguration
@ComponentScan(basePackageClasses = DataAutoConfiguration.class)
public class DataAutoConfiguration {
}
