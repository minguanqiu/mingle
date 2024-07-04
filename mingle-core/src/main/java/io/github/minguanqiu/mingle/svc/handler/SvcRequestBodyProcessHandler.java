package io.github.minguanqiu.mingle.svc.handler;

/**
 * Handler for request payload decryption.
 *
 * @author Qiu Guan Ming
 */

public interface SvcRequestBodyProcessHandler {

  /**
   * Pre-process request body.
   *
   * @param body the request body from input stream.
   * @return return the after processing body.
   */
  String processBody(String body);

}
