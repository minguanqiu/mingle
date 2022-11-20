package io.github.amings.mingle.svc.handler.impl;

import io.github.amings.mingle.svc.handler.SvcLogHandler;
import io.github.amings.mingle.svc.handler.model.SvcBeginModel;
import io.github.amings.mingle.svc.handler.model.SvcEndModel;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link SvcLogHandler} impl
 *
 * @author Ming
 */

@Slf4j
public class SvcLogHandlerDefaultImpl implements SvcLogHandler {

    @Override
    public void writeBeginLog(SvcBeginModel model) {
        log.info("【Svc PayLoad】 : " + model.getPayloadBody());
        log.info("【Svc Model】 : " + model.getModelBody());
    }

    @Override
    public void writeEndLog(SvcEndModel model) {
        log.info("【Svc Response】 : " + model.getResponseBody());
    }

}
