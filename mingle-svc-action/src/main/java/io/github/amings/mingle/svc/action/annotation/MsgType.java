package io.github.amings.mingle.svc.action.annotation;

import java.lang.annotation.*;

/**
 * @author Ming
 * 
 */

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MsgType {

    String value();

}
