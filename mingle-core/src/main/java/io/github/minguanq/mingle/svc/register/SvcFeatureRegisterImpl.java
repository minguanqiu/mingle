package io.github.minguanq.mingle.svc.register;

import io.github.minguanq.mingle.svc.Service;
import io.github.minguanq.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanq.mingle.svc.handler.SvcFeatureHandler;
import io.github.minguanq.mingle.svc.handler.model.SvcFeature;

import java.util.Map;

/**
 * @author Ming
 */
public class SvcFeatureRegisterImpl implements SvcFeatureRegister<SvcFeature> {

    private final SvcProperties svcProperties;

    private final SvcFeatureHandler svcFeatureHandler;

    public SvcFeatureRegisterImpl(SvcProperties svcProperties, SvcFeatureHandler svcFeatureHandler) {
        this.svcProperties = svcProperties;
        this.svcFeatureHandler = svcFeatureHandler;
    }

    @Override
    public boolean support(SvcRegister.SvcDefinition svcDefinition) {
        return true;
    }

    @Override
    public SvcFeature registerFeature(SvcRegister.SvcDefinition svcDefinition) {
        return buildFeature(svcDefinition);
    }

    private SvcFeature buildFeature(SvcRegister.SvcDefinition svcDefinition) {
        boolean logging = svcProperties.getFeature().isLogging();
        boolean bodyProcess = svcProperties.getFeature().isBodyProcess();
        String[] ipSecure = svcProperties.getFeature().getIpSecure();
        io.github.minguanq.mingle.svc.annotation.SvcFeature svcFeatureAnnotation = svcDefinition.getSvcClass().getAnnotation(io.github.minguanq.mingle.svc.annotation.SvcFeature.class); //second setting
        if (svcFeatureAnnotation != null) {
            logging = svcFeatureAnnotation.logging();
            bodyProcess = svcFeatureAnnotation.body_process();
            ipSecure = svcFeatureAnnotation.ip_secure();
        }
        Map<Class<? extends Service<?, ?>>, SvcFeature> svcFeatureMap = svcFeatureHandler.getSvcFeature(); //third setting
        if (svcFeatureMap.containsKey(svcDefinition.getSvcClass())) {
            SvcFeature svcFeature = svcFeatureMap.get(svcDefinition.getSvcClass());
            logging = svcFeature.logging();
            bodyProcess = svcFeature.body_process();
            if (svcFeature.ip_secure() != null) {
                ipSecure = svcFeature.ip_secure();
            }
        }
        return new SvcFeature(logging, bodyProcess, ipSecure);
    }

}
