package io.github.minguanqiu.mingle.svc.session.handler.impl;

import io.github.minguanqiu.mingle.svc.session.handler.TokenKeyHandler;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Default implementations for {@link TokenKeyHandler}.
 *
 * @author Qiu Guan Ming
 */
public class TokenKeyHandlerImpl implements TokenKeyHandler {

  private final SecretKey key;

  /**
   * Create a new TokenKeyHandlerImpl instance.
   */
  public TokenKeyHandlerImpl() {
    KeyGenerator keyGenerator;
    try {
      keyGenerator = KeyGenerator.getInstance("AES");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    keyGenerator.init(256);
    key = keyGenerator.generateKey();
  }

  @Override
  public SecretKey getAesSecretKey() {
    return key;
  }

}
