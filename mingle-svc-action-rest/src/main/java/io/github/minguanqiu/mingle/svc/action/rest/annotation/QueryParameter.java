package io.github.minguanqiu.mingle.svc.action.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation Help build uri path variable for restful action
 *
 * @author Qiu Guan Ming
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryParameter {

  String value() default "";

}
