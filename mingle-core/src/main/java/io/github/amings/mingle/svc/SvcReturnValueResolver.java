package io.github.amings.mingle.svc;

import io.github.amings.mingle.svc.filter.SvcInfo;
import io.github.amings.mingle.svc.utils.SvcResUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
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

@Deprecated
public class SvcReturnValueResolver extends RequestResponseBodyMethodProcessor {

    @Autowired
    SvcInfo svcInfo;
    @Autowired
    SvcResUtils svcResUtils;

    public SvcReturnValueResolver(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return SvcResModel.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws HttpMediaTypeNotAcceptableException, IOException {
        super.handleReturnValue(svcResUtils.build(svcInfo.getCode(), svcInfo.getDesc(), (SvcResModel) returnValue), returnType, mavContainer, webRequest);
    }

}
