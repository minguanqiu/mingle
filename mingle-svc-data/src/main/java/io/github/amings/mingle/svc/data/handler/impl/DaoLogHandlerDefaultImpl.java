package io.github.amings.mingle.svc.data.handler.impl;

import io.github.amings.mingle.svc.data.handler.DaoLogHandler;
import io.github.amings.mingle.svc.data.handler.model.DaoLogBeginModel;
import io.github.amings.mingle.svc.data.handler.model.DaoLogEndModel;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * {@link DaoLogHandler} impl
 *
 * @author Ming
 */

@Slf4j
public class DaoLogHandlerDefaultImpl implements DaoLogHandler {

    @Override
    public void writeBeginLog(DaoLogBeginModel model) {
        log.info("【Dao Request】 : " + model.getRequestBody());
    }

    @Override
    public void writeEndLog(DaoLogEndModel model) {
        log.info("【Dao Response】 : " + model.getResponseBody());
    }

    @Override
    public void afterThrowing(Throwable t, String uuid, LocalDateTime endDateTime, String runTime) {
        log.error("【Dao Exception】 : " + t.getMessage());
    }
}
