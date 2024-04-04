package io.github.minguanq.mingle.svc;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * Service response header model
 *
 * @author Ming
 */
@Data
@Builder
public class SvcResponseHeader {

    private String code;

    private String msg;

    private Map<String, String> convertMap;

    public static SvcResponseHeaderBuilder builder(String code) {
        return new SvcResponseHeaderBuilder().code(code);
    }

}
