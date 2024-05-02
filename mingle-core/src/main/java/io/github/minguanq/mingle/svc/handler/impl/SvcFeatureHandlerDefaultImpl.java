package io.github.minguanq.mingle.svc.handler.impl;

import io.github.minguanq.mingle.svc.Service;
import io.github.minguanq.mingle.svc.handler.SvcFeatureHandler;
import io.github.minguanq.mingle.svc.handler.model.SvcFeature;

import java.util.Map;

/**
 * @author Ming
 */
public class SvcFeatureHandlerDefaultImpl implements SvcFeatureHandler {

    @Override
    public Map<Class<? extends Service<?, ?>>, SvcFeature> getSvcFeature() {
        return Map.of();
    }

}
