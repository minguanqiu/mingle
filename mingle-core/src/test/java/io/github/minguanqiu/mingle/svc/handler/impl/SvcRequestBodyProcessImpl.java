package io.github.minguanqiu.mingle.svc.handler.impl;

import io.github.minguanqiu.mingle.svc.handler.SvcRequestBodyProcessHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Qiu Guan Ming
 */
@Component
public class SvcRequestBodyProcessImpl implements SvcRequestBodyProcessHandler {

  @Autowired
  HttpServletRequest httpServletRequest;

  @Override
  public String processBody(String body) {
    httpServletRequest.setAttribute(SvcRequestBodyProcessHandler.class.getSimpleName(), "true");
    return body;
  }

}
