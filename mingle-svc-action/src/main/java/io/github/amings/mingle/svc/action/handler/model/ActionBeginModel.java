package io.github.amings.mingle.svc.action.handler.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Action logging model
 *
 * @author Ming
 */

@Data
public class ActionBeginModel {

    private String svcSerialNum;

    private String actSerialNum;

    private LocalDateTime startDateTime;

}
