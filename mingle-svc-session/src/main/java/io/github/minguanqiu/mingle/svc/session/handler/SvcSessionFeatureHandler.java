package io.github.minguanqiu.mingle.svc.session.handler;

import io.github.minguanqiu.mingle.svc.Service;
import io.github.minguanqiu.mingle.svc.session.handler.model.SvcSessionFeature;
import java.util.Map;

/**
 * Handler for service session feature.
 *
 * @author Qiu Guan Ming
 */
public interface SvcSessionFeatureHandler {

  /**
   * Build service session feature.
   *
   * @return return the map of service session feature.
   */
  Map<Class<? extends Service<?, ?>>, SvcSessionFeature> getSvcFeature();

}
