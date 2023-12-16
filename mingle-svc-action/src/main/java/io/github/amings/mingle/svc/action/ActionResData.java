package io.github.amings.mingle.svc.action;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Action response data
 *
 * @author Ming
 */

@Getter
@Setter(AccessLevel.PROTECTED)
public class ActionResData<Res extends ActionResModel> {

    private String msgType;

    private String code;

    private String desc;

    private Res resModel;

    private boolean success;

}
