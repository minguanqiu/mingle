package io.github.amings.mingle.svc.handler;

import io.github.amings.mingle.svc.handler.model.ActionBeginModel;
import io.github.amings.mingle.svc.handler.model.ActionEndModel;

import java.time.LocalDateTime;

/**
 * Implements to custom action logging method，must be a spring bean
 *
 * @author Ming
 */

public interface ActionLogHandler {

    void writeBeginLog(ActionBeginModel model);

    void writeEndLog(ActionEndModel model);

    @Deprecated
    void afterThrowing(Throwable t, String uuid, LocalDateTime endDateTime, String runTime);

    default void afterThrowing(Throwable t, ActionEndModel model) {

    }

}
