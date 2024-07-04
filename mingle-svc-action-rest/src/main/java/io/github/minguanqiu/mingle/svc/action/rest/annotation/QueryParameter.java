package io.github.minguanqiu.mingle.svc.action.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for build path query parameter.
 *
 * @author Qiu Guan Ming
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryParameter {

  /**
   * Parameter key name.
   *
   * @return return the parameter key name.
   */
  String value() default "";

}
