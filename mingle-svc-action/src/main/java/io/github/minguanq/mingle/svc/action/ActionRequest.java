package io.github.minguanq.mingle.svc.action;

import io.github.minguanq.mingle.svc.action.enums.AutoBreak;
import lombok.Data;

/**
 * Base class for all action request
 *
 * @author Ming
 */

@Data
public class ActionRequest {

    private AutoBreak autoBreak = AutoBreak.GLOBAL;

}
