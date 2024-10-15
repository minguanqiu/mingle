package io.github.minguanqiu.mingle.svc.handler.impl;

import io.github.minguanqiu.mingle.svc.handler.SvcPathHandler;
import org.springframework.util.ClassUtils;

/**
 * Default implement for {@link SvcPathHandler}.
 *
 * @author Qiu Guan Ming
 */
public class SvcPathHandlerDefaultImpl implements SvcPathHandler {

  @Override
  public String getPath(Class<?> serviceClass) {
    return "/svc" + "/" + ClassUtils.getUserClass(serviceClass).getSimpleName();
  }

}
