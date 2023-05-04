package io.github.amings.mingle.svc;

import io.github.amings.mingle.svc.annotation.Svc;
import io.github.amings.mingle.svc.filter.SvcInfo;
import io.github.amings.mingle.svc.utils.SvcResUtils;
import io.github.amings.mingle.utils.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
 * Svc response resolver
 *
 * @author Ming
 */

public class SvcReturnValueResolver extends RequestResponseBodyMethodProcessor {

    @Autowired
    private SvcInfo svcInfo;
    @Autowired
    private SvcResUtils svcResUtils;
    @Autowired
    @Qualifier("svcLogJacksonUtils")
    private JacksonUtils jacksonUtils;

    public SvcReturnValueResolver(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return SvcResModel.class.isAssignableFrom(returnType.getParameterType()) && AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), Svc.class);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws HttpMediaTypeNotAcceptableException, IOException {
        if (svcInfo.getSvcResModelHandler4Log() == null) {
            svcInfo.setSvcResModelHandler4Log(svcResUtils.build(svcInfo.getCode(), svcInfo.getDesc(), (SvcResModel) returnValue, jacksonUtils));
        }
        super.handleReturnValue(svcResUtils.build(svcInfo.getCode(), svcInfo.getDesc(), (SvcResModel) returnValue), returnType, mavContainer, webRequest);
    }

}
