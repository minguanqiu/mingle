package io.github.minguanqiu.mingle.svc.annotation;

import io.github.minguanqiu.mingle.svc.AbstractService;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation provides the setting service function.
 * It allows enabling or disabling specific features of the service such as logging,
 * request body processing, and IP security.
 * <pre>
 * {@code
 * @example
 * @SvcFeature(logging = true, body_process = true, ip_secure = {"192.168.1.1"})
 * public class MyService extends AbstractService {
 *     // Service implementation
 * }
 * }
 *</pre>
 * @author Qiu Guan Ming
 * @see AbstractService
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SvcFeature {

  /**
   * If set true, will enable the logging feature.
   *
   * @return true if logging is enabled, false otherwise
   **/
  boolean logging() default false;

  /**
   * If set true, will enable the request body process feature.
   *
   * @return true if request body processing is enabled, false otherwise
   **/
  boolean body_process() default false;

  /**
   * If set, will enable the IP secure feature.
   * Specifies the IP addresses that are allowed access.
   *
   * @return an array of IP addresses for the IP secure feature
   **/
  String[] ip_secure() default {};

}

