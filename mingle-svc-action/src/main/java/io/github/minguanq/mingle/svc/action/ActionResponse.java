package io.github.minguanq.mingle.svc.action;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Action response data
 *
 * @author Ming
 */

@Getter
@Setter(AccessLevel.PACKAGE)
public class ActionResponse<ResB extends ActionResponseBody> {

    private boolean success;

    private String code;

    private String msg;

    private final Map<String, Object> values = new HashMap<>();

    private ResB responseBody;

    private String msgType;

    public Optional<ResB> getResponseBody() {
        return Optional.ofNullable(responseBody);
    }
}
