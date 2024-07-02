package io.github.minguanqiu.mingle.svc.redis;

import io.github.minguanqiu.mingle.svc.SvcResponseBody;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Qiu Guan Ming
 */
@Setter
@Getter
public class SimpleSvcRes extends SvcResponseBody {

  private String text1;

  private String text2;

}
