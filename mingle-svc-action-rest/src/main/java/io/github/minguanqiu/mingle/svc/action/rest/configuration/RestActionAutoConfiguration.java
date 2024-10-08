package io.github.minguanqiu.mingle.svc.action.rest.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;

/**
 * {@link AutoConfiguration} for restful action module.
 *
 * @author Qiu Guan Ming
 */
@AutoConfiguration
@AutoConfigurationPackage(basePackageClasses = RestActionAutoConfiguration.class)
public class RestActionAutoConfiguration {

}
