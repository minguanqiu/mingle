package io.github.minguanqiu.mingle.svc.session.handler;

import io.github.minguanqiu.mingle.svc.Service;
import io.github.minguanqiu.mingle.svc.session.handler.model.SvcSessionFeature;
import java.util.Map;

/**
 * @author Qiu Guan Ming
 */
public interface SvcSessionFeatureHandler {

  Map<Class<? extends Service<?, ?>>, SvcSessionFeature> getSvcFeature();

}
