package io.github.minguanq.mingle.svc.handler;

import io.github.minguanq.mingle.svc.handler.model.CodeMessage;

import java.util.List;

/**
 * Handler for service message,provide custom message list
 *
 * @author Ming
 */

public interface CodeMessageListHandler {

    List<CodeMessage> getMsgList();

}
