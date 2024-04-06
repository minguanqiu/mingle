package io.github.minguanq.mingle.svc.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;

import java.lang.annotation.*;

/**
 * Annotation for service register and configuration feature
 *
 * @author Ming
 */
@Documented
@Controller
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Svc {

    /**
     * Spring bean name
     **/
    @AliasFor(
            annotation = Controller.class
    )
    String value() default "";

    /**
     * Spring doc tags
     **/
    String[] tags() default {};

    /**
     * Spring doc summary
     **/
    String summary() default "";

    /**
     * Spring doc description
     **/
    String description() default "";

}
