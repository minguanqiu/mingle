package io.github.amings.mingle.svc.handler.impl;

import io.github.amings.mingle.svc.handler.ActionLogHandler;
import io.github.amings.mingle.svc.handler.model.ActionBeginModel;
import io.github.amings.mingle.svc.handler.model.ActionEndModel;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * {@link ActionLogHandler} impl
 *
 * @author Ming
 */

@Slf4j
public class ActionLogHandlerDefalutImpl implements ActionLogHandler {

    @Override
    public void writeBeginLog(ActionBeginModel model) {
        log.info("【Action Request】 : " + model.getRequestBody());
    }

    @Override
    public void writeEndLog(ActionEndModel model) {
        log.info("【Action Response】 : " + model.getResponseBody());
    }

    @Override
    public void afterThrowing(Throwable t, String uuid, LocalDateTime endDateTime, String runTime) {
        log.error("【Action Exception】 : " + t.getMessage());
    }
}
