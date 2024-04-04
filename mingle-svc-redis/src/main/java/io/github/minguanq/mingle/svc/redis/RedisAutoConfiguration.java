package io.github.minguanq.mingle.svc.redis;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * {@link AutoConfiguration} for redis module
 *
 * @author Ming
 */

@AutoConfiguration
@ComponentScan(basePackageClasses = RedisAutoConfiguration.class)
@PropertySource("classpath:svc-redis.properties")
public class RedisAutoConfiguration {
}
