package io.github.minguanqiu.mingle.svc.handler;

/**
 * Handler for request payload decryption
 *
 * @author Qiu Guan Ming
 */

public interface SvcRequestBodyProcessHandler {

  String processBody(String body);

}
