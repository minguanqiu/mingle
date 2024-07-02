package io.github.minguanqiu.mingle.svc.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties} mapping service properties.
 *
 * @author Qiu Guan Ming
 */
@Getter
@Setter
public class SvcProperties {

  /**
   * Service message type.
   */
  private String msgType = "svc";

  /**
   * Service successful code in response body.
   */
  private String code = "0000";

  /**
   * Service successful message in response body.
   */
  private String msg = "successful";

  private SvcFeature feature = new SvcFeature();

  /**
   * Service feature defined.
   */
  public static class SvcFeature {

    /**
     * Whether logging is enabled.
     */
    private boolean logging;

    /**
     * Whether body processing is enabled.
     */
    private boolean bodyProcess;

    /**
     * Secure IP addresses.
     */
    private String[] ipSecure = new String[]{};

    /**
     * Gets whether logging is enabled.
     *
     * @return true if logging is enabled, false otherwise.
     */
    public boolean isLogging() {
      return logging;
    }

    /**
     * Sets whether logging is enabled.
     *
     * @param logging true to enable logging, false to disable.
     */
    public void setLogging(boolean logging) {
      this.logging = logging;
    }

    /**
     * Gets whether body processing is enabled.
     *
     * @return true if body processing is enabled, false otherwise.
     */
    public boolean isBodyProcess() {
      return bodyProcess;
    }

    /**
     * Sets whether body processing is enabled.
     *
     * @param bodyProcess true to enable body processing, false to disable.
     */
    public void setBodyProcess(boolean bodyProcess) {
      this.bodyProcess = bodyProcess;
    }

    /**
     * Gets the secure IP addresses.
     *
     * @return the secure IP addresses.
     */
    public String[] getIpSecure() {
      return ipSecure;
    }

    /**
     * Sets the secure IP addresses.
     *
     * @param ipSecure the secure IP addresses.
     */
    public void setIpSecure(String[] ipSecure) {
      this.ipSecure = ipSecure;
    }
  }

}
