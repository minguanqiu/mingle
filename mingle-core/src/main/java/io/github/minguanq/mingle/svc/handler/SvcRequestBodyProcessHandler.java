package io.github.minguanq.mingle.svc.handler;

/**
 * Handler for request payload decryption
 *
 * @author Ming
 */

public interface SvcRequestBodyProcessHandler {

    String processBody(String body);

}
