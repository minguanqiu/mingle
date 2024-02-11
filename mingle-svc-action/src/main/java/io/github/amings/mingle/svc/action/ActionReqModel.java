package io.github.amings.mingle.svc.action;

import io.github.amings.mingle.svc.action.enums.AutoBreak;
import lombok.Data;

/**
 * Action request model
 *
 * @author Ming
 */

@Data
public class ActionReqModel {

    private AutoBreak autoBreak = AutoBreak.GLOBAL;

}
