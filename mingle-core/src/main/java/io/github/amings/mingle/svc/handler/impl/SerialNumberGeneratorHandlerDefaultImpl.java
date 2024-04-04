package io.github.amings.mingle.svc.handler.impl;

import io.github.amings.mingle.svc.handler.SerialNumberGeneratorHandler;

import java.util.UUID;

/**
 * {@inheritDoc}
 * Default impl for {@link SerialNumberGeneratorHandler}
 *
 * @author Ming
 */
public class SerialNumberGeneratorHandlerDefaultImpl implements SerialNumberGeneratorHandler {

    @Override
    public String generate(String type) {
        return UUID.randomUUID().toString();
    }

}
