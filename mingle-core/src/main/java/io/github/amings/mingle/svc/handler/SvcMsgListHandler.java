package io.github.amings.mingle.svc.handler;

import io.github.amings.mingle.svc.handler.model.SvcMsgModel;

import java.util.List;

/**
 * Handler for service message,provide custom message list
 *
 * @author Ming
 */

public interface SvcMsgListHandler {

    List<SvcMsgModel> getMsgList();

}
