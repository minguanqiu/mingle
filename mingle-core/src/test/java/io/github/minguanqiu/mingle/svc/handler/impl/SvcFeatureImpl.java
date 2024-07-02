package io.github.minguanqiu.mingle.svc.handler.impl;

import io.github.minguanqiu.mingle.svc.Service;
import io.github.minguanqiu.mingle.svc.SimpleSvcWithFeature1;
import io.github.minguanqiu.mingle.svc.handler.SvcFeatureHandler;
import io.github.minguanqiu.mingle.svc.handler.model.SvcFeature;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author Qiu Guan Ming
 */
@Component
@Profile("test-feature-handler")
public class SvcFeatureImpl implements SvcFeatureHandler {

  @Override
  public Map<Class<? extends Service<?, ?>>, SvcFeature> getSvcFeature() {
    return Map.of(SimpleSvcWithFeature1.class,
        new SvcFeature(true, true, new String[]{"127.0.0.1"}));
  }

}
