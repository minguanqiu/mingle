package io.github.amings.mingle.svc;

import io.github.amings.mingle.svc.annotation.Svc;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.io.IOException;
import java.util.List;

/**
 * Custom {@link RequestResponseBodyMethodProcessor} for service response body,only service response support
 *
 * @author Ming
 */

public class SvcResponseResolver extends RequestResponseBodyMethodProcessor {

    public SvcResponseResolver(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return SvcResponse.class.isAssignableFrom(returnType.getParameterType()) && AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), Svc.class);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws HttpMediaTypeNotAcceptableException, IOException {
        super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }

}
