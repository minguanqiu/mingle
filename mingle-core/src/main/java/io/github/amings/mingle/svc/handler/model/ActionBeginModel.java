package io.github.amings.mingle.svc.handler.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Action logging model
 *
 * @author Ming
 */

@Data
public class ActionBeginModel {

    private String uuid;

    private String name;

    private LocalDateTime startDateTime;

    private String requestBody;

    private String type;

}
