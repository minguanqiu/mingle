package io.github.minguanqiu.mingle.svc.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonView;
import io.github.minguanqiu.mingle.svc.json.view.Views;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link JsonView} for exclude logging serialization
 *
 * @author Ming
 */
@JacksonAnnotationsInside
@JsonView(Views.ExcludeLog.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcludeLog {
}
