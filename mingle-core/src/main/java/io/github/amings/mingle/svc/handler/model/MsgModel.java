package io.github.amings.mingle.svc.handler.model;

import lombok.Data;

/**
 * system message model
 *
 * @author Ming
 */

@Data
public class MsgModel {

    private String msgType;

    private String code;

    private String desc;

}
