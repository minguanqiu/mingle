package io.github.amings.mingle.svc.session.handler;

/**
 * Handler for session token encryption and decryption
 *
 * @author Ming
 */
public interface SessionTokenHandler {

    String encryption(String plainText) throws Exception;

    String decryption(String cipherText) throws Exception;

}
