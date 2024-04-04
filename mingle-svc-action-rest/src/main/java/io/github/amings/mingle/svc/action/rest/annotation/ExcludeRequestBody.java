package io.github.amings.mingle.svc.action.rest.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonView;
import io.github.amings.mingle.svc.action.rest.json.view.Views;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation help to hide target field
 *
 * @author Ming
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonView({Views.ExcludeRequestBody.class,io.github.amings.mingle.svc.json.view.Views.class})
public @interface ExcludeRequestBody {

}
