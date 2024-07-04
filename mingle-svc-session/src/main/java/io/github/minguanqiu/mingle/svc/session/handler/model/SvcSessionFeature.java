package io.github.minguanqiu.mingle.svc.session.handler.model;

/**
 * Service session feature model.
 *
 * @param types     the service session type.
 * @param authority the service authority.
 * @author Qiu Guan Ming
 */
public record SvcSessionFeature(String[] types, boolean authority) {

}
