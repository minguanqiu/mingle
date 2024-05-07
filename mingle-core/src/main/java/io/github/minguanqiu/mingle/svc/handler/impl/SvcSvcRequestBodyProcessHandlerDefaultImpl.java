package io.github.minguanqiu.mingle.svc.handler.impl;

import io.github.minguanqiu.mingle.svc.handler.SvcRequestBodyProcessHandler;

/**
 * {@inheritDoc}
 * Default impl for {@link SvcRequestBodyProcessHandler}
 *
 * @author Ming
 */
public class SvcSvcRequestBodyProcessHandlerDefaultImpl implements SvcRequestBodyProcessHandler {

    @Override
    public String processBody(String body) {
        return body;
    }

}
