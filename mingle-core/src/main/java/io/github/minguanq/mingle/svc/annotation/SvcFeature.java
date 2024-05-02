package io.github.minguanq.mingle.svc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provide service feature
 *
 * @author Ming
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SvcFeature {

    /**
     * If set ture,will enable logging feature
     **/
    boolean logging() default false;

    /**
     * If set ture,will enable request body process feature
     **/
    boolean body_process() default false;

    /**
     * If have value,will enable ip secure feature
     **/
    String[] ip_secure() default {};

}
