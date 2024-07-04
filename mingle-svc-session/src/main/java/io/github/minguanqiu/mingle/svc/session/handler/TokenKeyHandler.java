package io.github.minguanqiu.mingle.svc.session.handler;

import javax.crypto.SecretKey;

/**
 * Handler for generate aes key.
 *
 * @author Qiu Guan Ming
 */
public interface TokenKeyHandler {

  /**
   * Build aes secret key.
   *
   * @return return the aes secret key.
   */
  SecretKey getAesSecretKey();

}
