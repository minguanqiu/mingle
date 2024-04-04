package io.github.amings.mingle.svc.action.rest.annotation;

import io.github.amings.mingle.svc.action.annotation.Action;
import io.github.amings.mingle.svc.action.rest.enums.HttpMethod;
import org.springframework.core.annotation.AliasFor;
import org.springframework.http.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ming
 */

@Action
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestAction {

    /**
     * Bean name
     * @return String
     */
    @AliasFor(
            annotation = Action.class
    )
    String value() default "";

    /**
     * Action desc
     * @return String
     */
    @AliasFor(
            annotation = Action.class
    )
    String desc();

    /**
     * Http method
     * @return Http method
     */
    HttpMethod method() default HttpMethod.POST;

    String mediaType() default MediaType.APPLICATION_JSON_VALUE;

}
