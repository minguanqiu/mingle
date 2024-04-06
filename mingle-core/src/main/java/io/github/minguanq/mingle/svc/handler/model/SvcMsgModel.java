package io.github.minguanq.mingle.svc.handler.model;

import lombok.Getter;

/**
 * Model for service message
 *
 * @author Ming
 */

@Getter
public class SvcMsgModel {

    public SvcMsgModel(String msgType, String code, String msg) {
        this.msgType = msgType;
        this.code = code;
        this.msg = msg;
    }

    private final String msgType;

    private final String code;

    private final String msg;

}
