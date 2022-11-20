package io.github.amings.mingle.svc.redis.handler.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Redis logging model
 *
 * @author Ming
 */

@Data
public class RedisLogBeginModel {

    private String uuid;

    private String name;

    private LocalDateTime startDateTime;

    private String requestBody;

}
