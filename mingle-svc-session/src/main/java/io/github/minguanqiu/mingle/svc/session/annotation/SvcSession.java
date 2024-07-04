package io.github.minguanqiu.mingle.svc.session.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enable service security feature for authentication and authority.
 *
 * @author Qiu Guan Ming
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SvcSession {

  /**
   * Type of session,make sure session independence
   *
   * @return return the array of session types.
   **/
  String[] types();

  /**
   * Enable authority for service
   *
   * @return return the ture or false to determine service enable authority feature.
   **/
  boolean authority() default false;

}
