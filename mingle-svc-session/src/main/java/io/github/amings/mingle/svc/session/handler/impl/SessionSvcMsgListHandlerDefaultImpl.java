package io.github.amings.mingle.svc.session.handler.impl;

import io.github.amings.mingle.svc.handler.SvcMsgListHandler;
import io.github.amings.mingle.svc.handler.model.MsgModel;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link SvcMsgListHandler} impl
 *
 * @author Ming
 */
public class SessionSvcMsgListHandlerDefaultImpl implements SvcMsgListHandler {

    @Override
    public List<MsgModel> getMsgList() {
        List<MsgModel> msgModels = new ArrayList<>();
        msgModels.add(getMsgModel("MGS21","Missing Authorization Header"));
        msgModels.add(getMsgModel("MGS22","Decryption JWT fail"));
        msgModels.add(getMsgModel("MGS23","SessionInfo read value fail"));
        msgModels.add(getMsgModel("MGS24","Session type incorrect"));
        msgModels.add(getMsgModel("MGS25","Session not found"));
        msgModels.add(getMsgModel("MGS26","Session has been logout by another session"));
        return msgModels;
    }

    private MsgModel getMsgModel(String code, String desc) {
        MsgModel msgModel = new MsgModel();
        msgModel.setMsgType("svc");
        msgModel.setCode(code);
        msgModel.setDesc(desc);
        return msgModel;
    }

}
