package io.github.minguanqiu.mingle.svc.handler;

import io.github.minguanqiu.mingle.svc.Service;
import io.github.minguanqiu.mingle.svc.handler.model.SvcFeature;
import java.util.Map;

/**
 * @author Qiu Guan Ming
 */
public interface SvcFeatureHandler {

  Map<Class<? extends Service<?, ?>>, SvcFeature> getSvcFeature();

}
