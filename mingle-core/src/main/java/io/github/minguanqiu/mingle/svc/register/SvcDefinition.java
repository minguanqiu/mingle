package io.github.minguanqiu.mingle.svc.register;

import io.github.minguanqiu.mingle.svc.Service;
import io.github.minguanqiu.mingle.svc.annotation.Svc;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * This class storage service all information.
 *
 * @author Qiu Gaun Ming
 */
@Getter
@Setter(AccessLevel.PACKAGE)
public class SvcDefinition {

  /**
   * Service name.
   *
   * @return return the service name.
   */
  private String svcName;

  /**
   * Service path.
   *
   * @return return the service path.
   */
  private String svcPath;

  /**
   * Service object.
   */
  @Getter(AccessLevel.PACKAGE)
  private Service<?, ?> service;

  /**
   * Service class.
   *
   * @return return the service class.
   */
  private Class<?> svcClass;

  /**
   * Service request class.
   *
   * @return return the service request class.
   */
  private Class<?> requestClass;

  /**
   * Service response body class.
   *
   * @return return the service response body class.
   */
  private Class<?> responseClass;

  /**
   * Service method.
   */
  @Getter(AccessLevel.PACKAGE)
  private Method svcMethod;

  /**
   * Service annotation.
   *
   * @return return the service annotation.
   */
  private Svc svc;

  /**
   * Service features.
   *
   * @param features the map of features.
   * @return return the map of features.
   */
  @Getter(AccessLevel.PROTECTED)
  @Setter(AccessLevel.PROTECTED)
  private Map<Class<?>, Object> features = new HashMap<>();

  /**
   * Get service feature.
   *
   * @param feature the feature class.
   * @param <T>     the feature type.
   * @return return the optional feature object.
   */
  @SuppressWarnings("unchecked")
  public <T> Optional<T> getFeature(Class<T> feature) {
    return (Optional<T>) Optional.ofNullable(features.get(feature));
  }


}
