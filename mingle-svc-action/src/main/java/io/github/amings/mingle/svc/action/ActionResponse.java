package io.github.amings.mingle.svc.action;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ming
 */

@Getter
@Setter(AccessLevel.PACKAGE)
public class ActionResponse<ResData extends ActionResData, Res extends ActionResModel> {

    private boolean success;

    private String code;

    private String desc;

    private ResData resData;

    private Res resModel;

    private String msgType;

}
