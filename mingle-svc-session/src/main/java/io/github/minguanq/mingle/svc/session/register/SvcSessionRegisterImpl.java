package io.github.minguanq.mingle.svc.session.register;

import io.github.minguanq.mingle.svc.register.SvcFeatureRegister;
import io.github.minguanq.mingle.svc.register.SvcRegister;
import io.github.minguanq.mingle.svc.session.annotation.SvcSession;
import io.github.minguanq.mingle.svc.session.handler.SvcSessionFeatureHandler;
import io.github.minguanq.mingle.svc.session.handler.model.SvcSessionFeature;

/**
 * @author Ming
 */
public class SvcSessionRegisterImpl implements SvcFeatureRegister<SvcSessionFeature> {

    private final SvcSessionFeatureHandler sessionFeatureHandler;

    public SvcSessionRegisterImpl(SvcSessionFeatureHandler sessionFeatureHandler) {
        this.sessionFeatureHandler = sessionFeatureHandler;
    }

    @Override
    public boolean support(SvcRegister.SvcDefinition svcDefinition) {
        return svcDefinition.getSvcClass().isAnnotationPresent(SvcSession.class);
    }

    @Override
    public SvcSessionFeature registerFeature(SvcRegister.SvcDefinition svcDefinition) {
        return buildFeature(svcDefinition);
    }

    private SvcSessionFeature buildFeature(SvcRegister.SvcDefinition svcDefinition) {
        String[] types = new String[0];
        boolean authority = false;
        SvcSession svcSession = svcDefinition.getSvcClass().getAnnotation(SvcSession.class);
        if (svcSession != null) {
            types = svcSession.types();
            authority = svcSession.authority();
        }
        if (sessionFeatureHandler.getSvcFeature().containsKey(svcDefinition.getSvcClass())) {
            types = sessionFeatureHandler.getSvcFeature().get(svcDefinition.getSvcClass()).types();
            authority = sessionFeatureHandler.getSvcFeature().get(svcDefinition.getSvcClass()).authority();
        }
        if(types == null) {
            types = new String[0];
        }
        return new SvcSessionFeature(types, authority);
    }
}
