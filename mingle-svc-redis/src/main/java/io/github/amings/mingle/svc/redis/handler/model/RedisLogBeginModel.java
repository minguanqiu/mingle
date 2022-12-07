package io.github.amings.mingle.svc.redis.handler.model;

import io.github.amings.mingle.svc.log.SvcLogModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Redis logging model
 *
 * @author Ming
 */

@Getter
@Setter
public class RedisLogBeginModel extends SvcLogModel {

    private String svcUuid;

    private String uuid;

    private String name;

    private LocalDateTime startDateTime;

    private String requestBody;

}
