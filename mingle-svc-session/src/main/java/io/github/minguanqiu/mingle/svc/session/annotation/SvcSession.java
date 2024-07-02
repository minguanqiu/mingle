package io.github.minguanqiu.mingle.svc.session.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enable service security feature for authentication and authority
 *
 * @author Qiu Guan Ming
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SvcSession {

  /**
   * Type of session,make sure session independence
   **/
  String[] types();

  /**
   * Enable authority for service
   **/
  boolean authority() default false;

}
