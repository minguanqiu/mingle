package io.github.amings.mingle.svc.action.rest;

import io.github.amings.mingle.svc.action.AbstractAction;

/**
 * Base class for all system response model
 *
 * @author Ming
 */

public abstract class RestActionResModel {

    public static final String actionSuccessCode = AbstractAction.ACTION_SUCCESS_CODE;

    public static final String actionSuccessMsg = AbstractAction.ACTION_SUCCESS_MSG;

    abstract public String getCode();

    abstract public String getDesc();

}
