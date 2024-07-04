package io.github.minguanqiu.mingle.svc.handler.impl;

import io.github.minguanqiu.mingle.svc.handler.SerialNumberGeneratorHandler;
import java.util.UUID;

/**
 * Default implement for {@link SerialNumberGeneratorHandler}.
 *
 * @author Qiu Guan Ming
 */
public class SerialNumberGeneratorHandlerDefaultImpl implements SerialNumberGeneratorHandler {

  @Override
  public String generate(String type) {
    return UUID.randomUUID().toString();
  }

}
