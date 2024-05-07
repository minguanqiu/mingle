package io.github.minguanqiu.mingle.svc.handler.impl;

import io.github.minguanqiu.mingle.svc.handler.SvcPathHandler;

/**
 * {@inheritDoc}
 * Default impl for {@link SvcPathHandler}
 *
 * @author Ming
 */
public class SvcPathHandlerDefaultImpl implements SvcPathHandler {

    @Override
    public String getPath(Class<?> serviceClass) {
        return "/svc" + "/" + serviceClass.getSimpleName();
    }

}
