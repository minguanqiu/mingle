package io.github.minguanqiu.mingle.svc.handler;

/**
 * Handler for logging serial number.
 *
 * @author Qiu Guan Ming
 */
public interface SerialNumberGeneratorHandler {

  /**
   * Generate serial number.
   *
   * @param type the serial type.
   * @return return the serial number.
   */
  String generate(String type);

}
