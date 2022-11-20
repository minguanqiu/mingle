package io.github.amings.mingle.svc.handler.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Svc logging model
 *
 * @author Ming
 */

@Data
public class SvcEndModel {

    private String uuid;

    private LocalDateTime endDateTime;

    private String responseBody;

    private String code;

    private String desc;

    private String runTime;

}
