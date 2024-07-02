package io.github.minguanqiu.mingle.svc.session.handler;

import javax.crypto.SecretKey;

/**
 * Handler for token key
 *
 * @author Qiu Guan Ming
 */
public interface TokenKeyHandler {

  SecretKey getAesSecretKey();

}
