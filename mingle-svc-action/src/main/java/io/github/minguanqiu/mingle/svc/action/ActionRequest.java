package io.github.minguanqiu.mingle.svc.action;

import io.github.minguanqiu.mingle.svc.action.enums.AutoBreak;
import lombok.Data;

/**
 * Base class for all action request
 *
 * @author Qiu Guan Ming
 */

@Data
public class ActionRequest {

  private AutoBreak autoBreak = AutoBreak.GLOBAL;

}
