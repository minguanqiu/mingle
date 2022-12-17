package io.github.amings.mingle.svc;

import io.github.amings.mingle.svc.filter.SvcInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Svc response model resolver
 *
 * @author Ming
 */

public class SvcResArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    SvcInfo svcInfo;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return SvcResModel.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return parameter.getParameterType().getDeclaredConstructor().newInstance();
    }

}
