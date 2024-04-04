package io.github.minguanq.mingle.svc.action.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for action
 *
 * @author Ming
 */
@Service
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {

    @AliasFor(
            annotation = Service.class
    )
    String value() default "";

    String desc() default "";

}
