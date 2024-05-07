package io.github.minguanqiu.mingle.svc.handler;

/**
 * Handler for build service path, equivalent to request mapping path
 *
 * @author Ming
 */
public interface SvcPathHandler {

    String getPath(Class<?> serviceClass);

}
