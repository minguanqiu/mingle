package io.github.amings.mingle.svc.handler;

import io.github.amings.mingle.svc.handler.model.MsgModel;

import java.util.List;

/**
 * Implements to build message list to change response desc
 *
 * @author Ming
 */

public interface SvcMsgListHandler {

    List<MsgModel> getMsgList();

}
