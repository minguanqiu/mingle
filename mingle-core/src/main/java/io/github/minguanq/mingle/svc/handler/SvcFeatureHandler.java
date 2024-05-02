package io.github.minguanq.mingle.svc.handler;

import io.github.minguanq.mingle.svc.Service;
import io.github.minguanq.mingle.svc.handler.model.SvcFeature;

import java.util.Map;

/**
 * @author Ming
 */
public interface SvcFeatureHandler {

    Map<Class<? extends Service<?, ?>>, SvcFeature> getSvcFeature();


}
