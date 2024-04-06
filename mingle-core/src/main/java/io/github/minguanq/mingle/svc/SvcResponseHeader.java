package io.github.minguanq.mingle.svc;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * Service response header model
 *
 * @author Ming
 */
@Getter
@Builder
public class SvcResponseHeader {

    private String code;

    private String msg;

    private Map<String, String> convertMap;

    private SvcResponseHeader(String code, String msg, Map<String, String> convertMap) {
        assert code != null;
        this.code = code;
        this.msg = msg;
        this.convertMap = convertMap;
    }

    public static SvcResponseHeaderBuilder builder(String code) {
        return new SvcResponseHeaderBuilder().code(code);
    }

}
