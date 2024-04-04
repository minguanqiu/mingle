package io.github.amings.mingle.svc.handler.model;

import lombok.Data;

/**
 * Model for service message
 *
 * @author Ming
 */

@Data
public class SvcMsgModel {

    private String msgType;

    private String code;

    private String msg;

}
