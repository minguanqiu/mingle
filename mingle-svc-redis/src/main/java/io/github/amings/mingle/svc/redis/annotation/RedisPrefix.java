package io.github.amings.mingle.svc.redis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Redis key prefix
 *
 * @author Ming
 */


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisPrefix {

    /**
     * key prefix value
     * @return String
     */
    String value();

}
