package io.github.minguanqiu.mingle.svc.session.handler;

/**
 * Handler for session token encryption and decryption
 *
 * @author Qiu Guan Ming
 */
public interface SessionTokenHandler {

  String encryption(String plainText) throws Exception;

  String decryption(String cipherText) throws Exception;

}
