package io.github.amings.mingle.svc.handler;

/**
 * Implements to custom request body decryption，must be a spring bean
 *
 * @author Ming
 */

public interface PayLoadDecryptionHandler {

    String decryption(String body);

}
