package io.github.minguanq.mingle.svc;

import io.github.minguanq.mingle.svc.filter.SvcInfo;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Custom {@link HandlerMethodArgumentResolver} for service request argument,only service request support
 *
 * @author Ming
 */

public class SvcRequestArgumentResolver implements HandlerMethodArgumentResolver {

    private final SvcInfo svcInfo;

    public SvcRequestArgumentResolver(SvcInfo svcInfo) {
        this.svcInfo = svcInfo;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return SvcRequest.class.isAssignableFrom(parameter.getParameterType()) && (svcInfo.getSvcBinderModel() != null);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (svcInfo.getSvcReqModelValidFailException() != null) {
            throw svcInfo.getSvcReqModelValidFailException();
        }
        return svcInfo.getSvcRequest();
    }

}
