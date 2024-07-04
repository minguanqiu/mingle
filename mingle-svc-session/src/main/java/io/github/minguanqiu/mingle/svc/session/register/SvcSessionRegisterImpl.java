package io.github.minguanqiu.mingle.svc.session.register;

import io.github.minguanqiu.mingle.svc.register.SvcDefinition;
import io.github.minguanqiu.mingle.svc.register.SvcFeatureRegister;
import io.github.minguanqiu.mingle.svc.session.annotation.SvcSession;
import io.github.minguanqiu.mingle.svc.session.handler.SvcSessionFeatureHandler;
import io.github.minguanqiu.mingle.svc.session.handler.model.SvcSessionFeature;

/**
 * Register for service session feature implementations.
 *
 * @author Qiu Guan Ming
 */
public class SvcSessionRegisterImpl implements SvcFeatureRegister<SvcSessionFeature> {

  private final SvcSessionFeatureHandler sessionFeatureHandler;

  /**
   * Create a new SvcSessionRegisterImpl instance.
   *
   * @param sessionFeatureHandler the session feature handler.
   */
  public SvcSessionRegisterImpl(SvcSessionFeatureHandler sessionFeatureHandler) {
    this.sessionFeatureHandler = sessionFeatureHandler;
  }

  @Override
  public boolean support(SvcDefinition svcDefinition) {
    return svcDefinition.getSvcClass().isAnnotationPresent(SvcSession.class);
  }

  @Override
  public SvcSessionFeature registerFeature(SvcDefinition svcDefinition) {
    return buildFeature(svcDefinition);
  }

  private SvcSessionFeature buildFeature(SvcDefinition svcDefinition) {
    String[] types = new String[0];
    boolean authority = false;
    SvcSession svcSession = svcDefinition.getSvcClass().getAnnotation(SvcSession.class);
    if (svcSession != null) {
      types = svcSession.types();
      authority = svcSession.authority();
    }
    if (sessionFeatureHandler.getSvcFeature().containsKey(svcDefinition.getSvcClass())) {
      types = sessionFeatureHandler.getSvcFeature().get(svcDefinition.getSvcClass()).types();
      authority = sessionFeatureHandler.getSvcFeature().get(svcDefinition.getSvcClass())
          .authority();
    }
    if (types == null) {
      types = new String[0];
    }
    return new SvcSessionFeature(types, authority);
  }
}
