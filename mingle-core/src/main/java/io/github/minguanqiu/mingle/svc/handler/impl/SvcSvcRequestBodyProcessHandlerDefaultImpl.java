package io.github.minguanqiu.mingle.svc.handler.impl;

import io.github.minguanqiu.mingle.svc.handler.SvcRequestBodyProcessHandler;

/**
 * Default implement for {@link SvcRequestBodyProcessHandler}.
 *
 * @author Qiu Guan Ming
 */
public class SvcSvcRequestBodyProcessHandlerDefaultImpl implements SvcRequestBodyProcessHandler {

  @Override
  public String processBody(String body) {
    return body;
  }

}
