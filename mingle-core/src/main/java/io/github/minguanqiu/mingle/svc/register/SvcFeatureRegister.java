package io.github.minguanqiu.mingle.svc.register;

/**
 * Register for service feature.
 *
 * @author Qiu Guan Ming
 */
public interface SvcFeatureRegister<T> {

  /**
   * Whether to pass registerFeature func.
   *
   * @param svcDefinition the service definition.
   * @return return the ture or false to pass.
   */
  boolean support(SvcDefinition svcDefinition);

  /**
   * Register service feature.
   *
   * @param svcDefinition the service definition.
   * @return return a register feature object.
   */
  T registerFeature(SvcDefinition svcDefinition);

}
