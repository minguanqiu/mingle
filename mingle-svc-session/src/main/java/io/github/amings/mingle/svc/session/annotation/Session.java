package io.github.amings.mingle.svc.session.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Session security feature
 *
 * @author Ming
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Session {

    /**
     * Session type
     * @return String
     **/
    String value();

    /**
     * Authority valid
     * @return boolean
     **/
    boolean authority() default false;

}
