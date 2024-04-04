package io.github.amings.mingle.svc.redis.annotation;

import java.lang.annotation.*;

/**
 * Define redis prefix key on redis entity
 *
 * @author Ming
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisPrefix {

    /**
     * Prefix of key
     */
    String value();

}
