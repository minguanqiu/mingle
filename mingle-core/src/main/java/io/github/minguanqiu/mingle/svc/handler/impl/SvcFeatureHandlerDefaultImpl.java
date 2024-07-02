package io.github.minguanqiu.mingle.svc.handler.impl;

import io.github.minguanqiu.mingle.svc.Service;
import io.github.minguanqiu.mingle.svc.handler.SvcFeatureHandler;
import io.github.minguanqiu.mingle.svc.handler.model.SvcFeature;
import java.util.Map;

/**
 * @author Qiu Guan Ming
 */
public class SvcFeatureHandlerDefaultImpl implements SvcFeatureHandler {

  @Override
  public Map<Class<? extends Service<?, ?>>, SvcFeature> getSvcFeature() {
    return Map.of();
  }

}
