package io.github.minguanq.mingle.svc.action;

import io.github.minguanq.mingle.svc.SvcAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.ComponentScan;

/**
 * {@link AutoConfiguration} for action
 *
 * @author Ming
 */

@AutoConfiguration
@AutoConfigurationPackage(basePackageClasses = ActionAutoConfiguration.class)
@ComponentScan(basePackageClasses = SvcAutoConfiguration.class)
public class ActionAutoConfiguration {


}
