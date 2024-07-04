package io.github.minguanqiu.mingle.svc.register;

import io.github.minguanqiu.mingle.svc.Service;
import io.github.minguanqiu.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanqiu.mingle.svc.handler.SvcFeatureHandler;
import io.github.minguanqiu.mingle.svc.handler.model.SvcFeature;
import java.util.Map;

/**
 * Implement for {@link SvcFeatureRegister}.
 *
 * @author Qiu Guan Ming
 */
public class SvcFeatureRegisterImpl implements SvcFeatureRegister<SvcFeature> {

  private final SvcProperties svcProperties;

  private final SvcFeatureHandler svcFeatureHandler;

  /**
   * Create a new SvcFeatureRegisterImpl instance.
   *
   * @param svcProperties     the service properties.
   * @param svcFeatureHandler the service feature handler.
   */
  public SvcFeatureRegisterImpl(SvcProperties svcProperties, SvcFeatureHandler svcFeatureHandler) {
    this.svcProperties = svcProperties;
    this.svcFeatureHandler = svcFeatureHandler;
  }

  @Override
  public boolean support(SvcDefinition svcDefinition) {
    return true;
  }

  @Override
  public SvcFeature registerFeature(SvcDefinition svcDefinition) {
    return buildFeature(svcDefinition);
  }

  /**
   * Build service feature. The priority is properties < annotation < handler.
   *
   * @param svcDefinition the service definition.
   * @return return the service feature.
   */
  private SvcFeature buildFeature(SvcDefinition svcDefinition) {
    boolean logging = svcProperties.getFeature().isLogging();
    boolean bodyProcess = svcProperties.getFeature().isBodyProcess();
    String[] ipSecure = svcProperties.getFeature().getIpSecure();
    io.github.minguanqiu.mingle.svc.annotation.SvcFeature svcFeatureAnnotation = svcDefinition.getSvcClass()
        .getAnnotation(
            io.github.minguanqiu.mingle.svc.annotation.SvcFeature.class); //second priority setting
    if (svcFeatureAnnotation != null) {
      logging = svcFeatureAnnotation.logging();
      bodyProcess = svcFeatureAnnotation.body_process();
      ipSecure = svcFeatureAnnotation.ip_secure();
    }
    Map<Class<? extends Service<?, ?>>, SvcFeature> svcFeatureMap = svcFeatureHandler.getSvcFeature(); //third priority setting
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
