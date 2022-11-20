package io.github.amings.mingle.svc.handler.impl;

import io.github.amings.mingle.svc.handler.PayLoadDecryptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link PayLoadDecryptionHandler} impl
 *
 * @author Ming
 */

@Slf4j
public class PayLoadDecryptionHandlerDefaultImpl implements PayLoadDecryptionHandler {

    @Override
    public String decryption(String body) {
        return body;
    }

}
