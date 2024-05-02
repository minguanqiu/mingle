package io.github.minguanq.mingle.svc.session.handler;

import io.github.minguanq.mingle.svc.Service;
import io.github.minguanq.mingle.svc.session.handler.model.SvcSessionFeature;

import java.util.Map;

/**
 * @author Ming
 */
public interface SvcSessionFeatureHandler {

    Map<Class<? extends Service<?, ?>>, SvcSessionFeature> getSvcFeature();

}
