package io.github.minguanqiu.mingle.svc;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

/**
 * Service response header model
 *
 * @author Qiu Guan Ming
 */
@Getter
@Builder
public class SvcResponseHeader {

  private String code;

  private String msg;

  private Map<String, String> convertMap;

  private SvcResponseHeader(String code, String msg, Map<String, String> convertMap) {
    assert code != null;
    this.code = code;
    this.msg = msg;
    this.convertMap = convertMap;
  }

  public static SvcResponseHeaderBuilder builder(String code) {
    return new SvcResponseHeaderBuilder().code(code);
  }

}
