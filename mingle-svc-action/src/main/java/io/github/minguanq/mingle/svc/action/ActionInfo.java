package io.github.minguanq.mingle.svc.action;

import lombok.Data;

import java.util.Map;

/**
 * This class provide when execute action process logic can controller result
 *
 * @author Ming
 */
@Data
public class ActionInfo<ResB extends ActionResponseBody> {

    private String code;

    private String msg;

    private Map<String, Object> values;

    private ResB responseBody;

}
