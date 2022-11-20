package io.github.amings.mingle.svc.data.handler.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Logging model
 *
 * @author Ming
 */

@Data
public class DaoLogBeginModel {

    private String uuid;

    private String name;

    private LocalDateTime startDateTime;

    private String requestBody;

}
