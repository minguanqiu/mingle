package io.github.amings.mingle.svc.handler.impl;

import io.github.amings.mingle.svc.handler.SvcRequestBodyProcessHandler;

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
