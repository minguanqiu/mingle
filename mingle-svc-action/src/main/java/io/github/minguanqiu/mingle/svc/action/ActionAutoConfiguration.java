package io.github.minguanqiu.mingle.svc.action;

import io.github.minguanqiu.mingle.svc.SvcAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.ComponentScan;

/**
 * {@link AutoConfiguration} for action
 *
 * @author Qiu Guan Ming
 */

@AutoConfiguration
@AutoConfigurationPackage(basePackageClasses = ActionAutoConfiguration.class)
@ComponentScan(basePackageClasses = SvcAutoConfiguration.class)
public class ActionAutoConfiguration {


}
