package io.github.minguanqiu.mingle.svc.session.handler.impl;

import io.github.minguanqiu.mingle.svc.Service;
import io.github.minguanqiu.mingle.svc.session.SimpleSvc;
import io.github.minguanqiu.mingle.svc.session.handler.SvcSessionFeatureHandler;
import io.github.minguanqiu.mingle.svc.session.handler.model.SvcSessionFeature;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author Qiu Guan Ming
 */
@Profile("test-session-feature")
@Component
public class SvcSessionFeatureImpl implements SvcSessionFeatureHandler {

  @Override
  public Map<Class<? extends Service<?, ?>>, SvcSessionFeature> getSvcFeature() {
    return Map.of(SimpleSvc.class, new SvcSessionFeature(new String[]{}, true));
  }

}
