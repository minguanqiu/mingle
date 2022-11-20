package io.github.amings.mingle.svc.data.handler;

import io.github.amings.mingle.svc.data.handler.model.DaoLogBeginModel;
import io.github.amings.mingle.svc.data.handler.model.DaoLogEndModel;

import java.time.LocalDateTime;

/**
 * Dao logging handler
 *
 * @author Ming
 */

public interface DaoLogHandler {

    void writeBeginLog(DaoLogBeginModel model);

    void writeEndLog(DaoLogEndModel model);

    void afterThrowing(Throwable t, String uuid, LocalDateTime endDateTime, String runTime);

}
