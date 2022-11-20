package io.github.amings.mingle.svc.session.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Save session info
 *
 * @author Ming
 */

@Data
public class SessionInfo {

    @JsonProperty("key")
    private String key;

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("timeToLive")
    private String timeToLive;

    @JsonProperty("single")
    private boolean single;

}
