package io.github.amings.mingle.svc.action.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation Help build form-data or query parameter request body for restful action
 *
 *
 * @author Ming
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BodyVariable {

    String value();

}
