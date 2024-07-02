package io.github.minguanqiu.mingle.svc.register;

/**
 * @author Qiu Guan Ming
 */
public interface SvcFeatureRegister<T> {

  boolean support(SvcRegister.SvcDefinition svcDefinition);

  T registerFeature(SvcRegister.SvcDefinition svcDefinition);

}
