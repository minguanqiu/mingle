package io.github.minguanq.mingle.svc.session.handler.impl;

import io.github.minguanq.mingle.svc.Service;
import io.github.minguanq.mingle.svc.session.handler.SvcSessionFeatureHandler;
import io.github.minguanq.mingle.svc.session.handler.model.SvcSessionFeature;

import java.util.Map;

/**
 * @author Ming
 */
public class SvcSessionFeatureDefaultImpl implements SvcSessionFeatureHandler {

    @Override
    public Map<Class<? extends Service<?, ?>>, SvcSessionFeature> getSvcFeature() {
        return Map.of();
    }

}
