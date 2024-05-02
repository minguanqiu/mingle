package io.github.minguanq.mingle.svc.action;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Map;

/**
 * This class provide when execute action process logic can controller result
 *
 * @author Ming
 */
@Data
public class ActionInfo {

    private String code;

    private String msg;

    @Setter(AccessLevel.PROTECTED)
    private Map<String, Object> values;

}
