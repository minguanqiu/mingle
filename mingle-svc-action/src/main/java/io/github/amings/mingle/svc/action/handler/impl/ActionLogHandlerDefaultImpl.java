package io.github.amings.mingle.svc.action.handler.impl;

import io.github.amings.mingle.svc.action.*;
import io.github.amings.mingle.svc.action.handler.ActionLogHandler;
import io.github.amings.mingle.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link ActionLogHandler} impl
 *
 * @author Ming
 */

@Slf4j
public class ActionLogHandlerDefaultImpl implements ActionLogHandler {

    private final JacksonUtils jacksonUtils;

    public ActionLogHandlerDefaultImpl(JacksonUtils jacksonUtils) {
        this.jacksonUtils = jacksonUtils;
    }


    @Override
    public void writeBeginLog(AbstractAction<? extends ActionReqModel, ? extends ActionResData, ? extends ActionResModel> action, ActionReqModel actionReqModel) {

    }

    @Override
    public void writeEndLog(AbstractAction<? extends ActionReqModel, ? extends ActionResData, ? extends ActionResModel> action, ActionResponse<? extends ActionResData, ? extends ActionResModel> actionResponse) {

    }

}
