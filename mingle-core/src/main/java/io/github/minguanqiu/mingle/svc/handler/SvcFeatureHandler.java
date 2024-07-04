package io.github.minguanqiu.mingle.svc.handler;

import io.github.minguanqiu.mingle.svc.Service;
import io.github.minguanqiu.mingle.svc.handler.model.SvcFeature;
import java.util.Map;

/**
 * Handler for service feature.
 *
 * @author Qiu Guan Ming
 */
public interface SvcFeatureHandler {

  /**
   * Build service feature.
   *
   * @return return a mpa of service feature.
   */
  Map<Class<? extends Service<?, ?>>, SvcFeature> getSvcFeature();

}
