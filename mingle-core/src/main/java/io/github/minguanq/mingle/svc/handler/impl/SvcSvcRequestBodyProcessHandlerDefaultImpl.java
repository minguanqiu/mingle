package io.github.minguanq.mingle.svc.handler.impl;

import io.github.minguanq.mingle.svc.handler.SvcRequestBodyProcessHandler;

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
