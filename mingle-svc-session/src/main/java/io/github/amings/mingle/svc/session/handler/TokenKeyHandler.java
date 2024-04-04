package io.github.amings.mingle.svc.session.handler;

import javax.crypto.SecretKey;

/**
 * Handler for token key
 *
 * @author Ming
 */
public interface TokenKeyHandler {

    SecretKey getAesSecretKey();

}
