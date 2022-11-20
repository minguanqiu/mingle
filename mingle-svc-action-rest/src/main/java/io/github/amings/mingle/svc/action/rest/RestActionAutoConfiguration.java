package io.github.amings.mingle.svc.action.rest;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Rest action auto configuration
 *
 * @author Ming
 */

@AutoConfiguration
@ComponentScan(basePackageClasses = RestActionAutoConfiguration.class)
public class RestActionAutoConfiguration {
}
