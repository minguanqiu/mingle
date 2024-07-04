package io.github.minguanqiu.mingle.svc.handler;

/**
 * Handler for build service path, equivalent to request mapping path.
 *
 * @author Qiu Guan Ming
 */
public interface SvcPathHandler {

  /**
   * Build custom service path.
   *
   * @param serviceClass the service class.
   * @return return the service path.
   */
  String getPath(Class<?> serviceClass);

}
