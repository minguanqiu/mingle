package io.github.amings.mingle.svc.redis;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * Redis auto configuration
 *
 * @author Ming
 */

@AutoConfiguration
@ComponentScan(basePackageClasses = RedisAutoConfiguration.class)
@PropertySource("classpath:svc-redis.properties")
public class RedisAutoConfiguration {
}
