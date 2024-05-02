package io.github.minguanq.mingle.svc.session.handler.impl;

import io.github.minguanq.mingle.svc.Service;
import io.github.minguanq.mingle.svc.session.SimpleSvc;
import io.github.minguanq.mingle.svc.session.handler.SvcSessionFeatureHandler;
import io.github.minguanq.mingle.svc.session.handler.model.SvcSessionFeature;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Ming
 */
@Profile("test-session-feature")
@Component
public class SvcSessionFeatureImpl implements SvcSessionFeatureHandler {

    @Override
    public Map<Class<? extends Service<?, ?>>, SvcSessionFeature> getSvcFeature() {
        return Map.of(SimpleSvc.class, new SvcSessionFeature(new String[]{}, true));
    }

}
