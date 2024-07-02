package io.github.minguanqiu.mingle.svc.handler.impl;

import io.github.minguanqiu.mingle.svc.handler.SvcPathHandler;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author Qiu Gaun Ming
 */
@Component
@Profile("test-svcPath-handler")
public class SvcPathHandlerImpl implements SvcPathHandler {

  @Override
  public String getPath(Class<?> serviceClass) {
    return "/testSvc" + "/" + serviceClass.getSimpleName();
  }
}
