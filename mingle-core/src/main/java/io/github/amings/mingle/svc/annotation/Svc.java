package io.github.amings.mingle.svc.annotation;

import io.github.amings.mingle.svc.handler.PayLoadDecryptionHandler;
import org.springframework.stereotype.Controller;

import java.lang.annotation.*;

/**
 * Main annotation for configuration Svc feature
 *
 * @author Ming
 */

@Documented
@Controller
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Svc {

    /**
     * @return String[]
     * Tag
     **/
    String[] tags() default {};

    /**
     * @return String
     * Summary
     **/
    String summary() default "";

    /**
     * @return String
     * Description
     **/
    String desc() default "";

    /**
     * @return String
     * Path
     **/
    String path() default "";

    /**
     * @return boolean
     * Logging Svc request and response body
     **/
    boolean log() default true;

    /**
     * @return boolean
     * Request body must encryption , implement {@link PayLoadDecryptionHandler} to decryption
     **/
    boolean encryption() default false;

    /**
     * @return boolean
     * If set ture,must configuration in properties
     **/
    boolean ipAddressSecure() default false;

    /**
     * @return boolean
     * Defined custom controller to escape Svc limit
     **/
    @Deprecated
    boolean custom() default false;
}
