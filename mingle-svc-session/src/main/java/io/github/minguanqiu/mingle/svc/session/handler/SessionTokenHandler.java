package io.github.minguanqiu.mingle.svc.session.handler;

/**
 * Handler for session token encryption and decryption
 *
 * @author Qiu Guan Ming
 */
public interface SessionTokenHandler {

  /**
   * Encryption for session token.
   *
   * @param plainText the session information.
   * @return return the encryption token string.
   * @throws Exception when encryption fail.
   */
  String encryption(String plainText) throws Exception;

  /**
   * Decryption for session token.
   *
   * @param cipherText the encryption session information.
   * @return return the decryption token string.
   * @throws Exception when decryption fail.
   */
  String decryption(String cipherText) throws Exception;

}
