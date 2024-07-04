package io.github.minguanqiu.mingle.svc.configuration.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * Service feature defined.
 *
 * @author Qiu Gaun Ming
 */
@Getter
@Setter
public class SvcFeature {

  /**
   * Whether logging is enabled.
   *
   * @param logging true to enable logging, false to disable.
   * @return true if logging is enabled, false otherwise.
   */
  private boolean logging;

  /**
   * Whether body processing is enabled.
   *
   * @param bodyProcess true to enable body processing, false to disable.
   * @return true if body processing is enabled, false otherwise.
   */
  private boolean bodyProcess;

  /**
   * Secure IP addresses.
   *
   * @param ipSecure the secure IP addresses.
   * @return the secure IP addresses.
   */
  private String[] ipSecure = new String[]{};

}
