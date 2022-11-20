package io.github.amings.mingle.svc.redis.handler.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Redis logging model
 *
 * @author Ming
 */

@Data
public class RedisLogEndModel {

    private String uuid;

    private LocalDateTime endDateTime;

    private String responseBody;

    private String runTime;

}
