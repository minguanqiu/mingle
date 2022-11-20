package io.github.amings.mingle.svc.annotation;

import io.github.amings.mingle.svc.handler.PayLoadDecryptionHandler;
import org.springframework.stereotype.Service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * main annotation for configuration Svc feature
 *
 * @author Ming
 */

@Documented
@Service
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Svc {

    /**
     * @return String
     * Svc description
     **/
    String desc();

    /**
     * @return String
     * Svc path
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
    boolean custom() default false;
}
