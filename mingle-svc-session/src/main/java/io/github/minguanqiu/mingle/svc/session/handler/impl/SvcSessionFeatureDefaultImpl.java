package io.github.minguanqiu.mingle.svc.session.handler.impl;

import io.github.minguanqiu.mingle.svc.Service;
import io.github.minguanqiu.mingle.svc.session.handler.SvcSessionFeatureHandler;
import io.github.minguanqiu.mingle.svc.session.handler.model.SvcSessionFeature;
import java.util.Map;

/**
 * Default implementations for {@link SvcSessionFeatureHandler}.
 *
 * @author Qiu Guan Ming
 */
public class SvcSessionFeatureDefaultImpl implements SvcSessionFeatureHandler {

  @Override
  public Map<Class<? extends Service<?, ?>>, SvcSessionFeature> getSvcFeature() {
    return Map.of();
  }

}
