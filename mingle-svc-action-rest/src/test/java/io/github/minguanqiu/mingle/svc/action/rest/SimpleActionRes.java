package io.github.minguanqiu.mingle.svc.action.rest;

import io.github.minguanqiu.mingle.svc.action.ActionResponseBody;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Qiu Guan Ming
 */
@Getter
@Setter
public class SimpleActionRes extends ActionResponseBody {

  private String text1;

  private String text2;

}
