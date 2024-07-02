package io.github.minguanqiu.mingle.svc.annotation;

import io.github.minguanqiu.mingle.svc.AbstractService;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;

/**
 * Annotation for service register.
 * This annotation is used to mark a class as a service controller and provides additional.
 * <p>
 * It is a Spring MVC controller.
 * <p>
 * It is a OpenAPI doc.
 * <p>
 * <pre>
 * {@code
 * @example
 * @Svc(value = "myService", tags = {"tag1", "tag2"}, summary = "This is a summary", description = "This is a detailed description")
 * public class MyService extends AbstractService {
 *     // Service methods
 * }
 * }
 * </pre>
 * @see Controller
 * @see io.swagger.v3.oas.annotations.Operation
 * @see AbstractService
 *
 * @author Qiu Guan Ming
 */
@Documented
@Controller
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Svc {

  /**
   * Alias for {@link Controller} value.
   * This value is used as the name of the controller in Spring.
   *
   * @return the name of the service controller
   */
  @AliasFor(annotation = Controller.class)
  String value() default "";

  /**
   * Mapping Spring doc tags.
   *
   * @return an array of tags
   */
  String[] tags() default {};

  /**
   * Mapping Spring doc summary.
   *
   * @return the summary of the service
   */
  String summary() default "";

  /**
   * Mapping Spring doc description.
   *
   * @return the description of the service
   */
  String description() default "";

}
