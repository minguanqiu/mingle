package io.github.minguanqiu.mingle.svc;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

/**
 * Service response header.
 *
 * @author Qiu Guan Ming
 */
@Getter
@Builder
public class SvcResponseHeader {

  /**
   * Service response code.
   *
   * @param code the service response code.
   * @return return the service response code.
   */
  private String code;

  /**
   * Service response message.
   *
   * @param msg the service response message.
   * @return return the service response message.
   */
  private String msg;

  /**
   * Service response message template convert map.
   *
   * @param convertMap the convert map.
   * @return return the service response message template convert map.
   */
  private Map<String, String> convertMap;

  /**
   * Create a new SvcResponseHeader instance.
   *
   * @param code       the service response code.
   * @param msg        the service response message.
   * @param convertMap the message template convert map.
   */
  private SvcResponseHeader(String code, String msg, Map<String, String> convertMap) {
    assert code != null;
    this.code = code;
    this.msg = msg;
    this.convertMap = convertMap;
  }

  /**
   * Service response header builder.
   *
   * @param code the service response code.
   * @return return the builder.
   */
  public static SvcResponseHeaderBuilder builder(String code) {
    return new SvcResponseHeaderBuilder().code(code);
  }

}
