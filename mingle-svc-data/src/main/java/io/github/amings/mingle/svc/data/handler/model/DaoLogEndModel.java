package io.github.amings.mingle.svc.data.handler.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Logging model
 *
 * @author Ming
 */

@Data
public class DaoLogEndModel {

    private String svcUuid;

    private String uuid;

    private LocalDateTime endDateTime;

    private String responseBody;

    private String runTime;

}
