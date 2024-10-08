package io.github.minguanqiu.mingle.svc.filter;

import io.github.minguanqiu.mingle.svc.Service;
import io.github.minguanqiu.mingle.svc.SvcRequest;
import io.github.minguanqiu.mingle.svc.SvcResponseBody;
import java.io.IOException;
import java.util.List;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

/**
 * Custom {@link RequestResponseBodyMethodProcessor} for service request argument and response
 * return value
 *
 * @author Qiu Guan Ming
 */
public class SvcRequestResponseBodyMethodProcessor extends RequestResponseBodyMethodProcessor {

  private final SvcInfo svcInfo;

  /**
   * Create a new SvcRequestResponseBodyMethodProcessor instance.
   *
   * @param converters the convert list.
   * @param svcInfo    the service information.
   */
  public SvcRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters,
      SvcInfo svcInfo) {
    super(converters);
    this.svcInfo = svcInfo;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return SvcRequest.class.isAssignableFrom(parameter.getParameterType())
        && Service.class.isAssignableFrom(parameter.getContainingClass());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    return svcInfo.getSvcRequest();
  }

  @Override
  public boolean supportsReturnType(MethodParameter returnType) {
    return SvcResponseBody.class.isAssignableFrom(returnType.getParameterType())
        && Service.class.isAssignableFrom(returnType.getContainingClass());
  }

  @Override
  public void handleReturnValue(Object returnValue, MethodParameter returnType,
      ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
      throws HttpMediaTypeNotAcceptableException, IOException {
    svcInfo.setSvcResponseBody(returnValue);
    super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
  }

}
