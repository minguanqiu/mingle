package io.github.amings.mingle.svc.action;

import io.github.amings.mingle.svc.action.annotation.AutoBreak;
import lombok.Data;

/**
 * @author Ming
 */

@Data
public class ActionReqData {

    private AutoBreak autoBreak = AutoBreak.GLOBAL;

}
