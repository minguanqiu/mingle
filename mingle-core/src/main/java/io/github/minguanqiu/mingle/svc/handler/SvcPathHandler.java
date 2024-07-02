package io.github.minguanqiu.mingle.svc.handler;

/**
 * Handler for build service path, equivalent to request mapping path
 *
 * @author Qiu Guan Ming
 */
public interface SvcPathHandler {

  String getPath(Class<?> serviceClass);

}
