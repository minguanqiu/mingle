package io.github.minguanqiu.mingle.svc;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * {@link AutoConfiguration} for service.
 *
 * @author Qiu Guan Ming
 */

@AutoConfiguration
@ComponentScan(basePackageClasses = SvcAutoConfiguration.class)
public class SvcAutoConfiguration {

}
