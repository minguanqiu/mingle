package io.github.amings.mingle.svc.handler;

/**
 * Handler for request payload decryption
 *
 * @author Ming
 */

public interface SvcRequestBodyProcessHandler {

    String processBody(String body);

}
