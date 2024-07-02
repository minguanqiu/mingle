package io.github.minguanqiu.mingle.svc.action;

import io.github.minguanqiu.mingle.svc.SvcRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Qiu Guan Ming
 */
@Setter
@Getter
public class SimpleSvcReq extends SvcRequest {

  private String action;

  private String text1;

  private String text2;

}
