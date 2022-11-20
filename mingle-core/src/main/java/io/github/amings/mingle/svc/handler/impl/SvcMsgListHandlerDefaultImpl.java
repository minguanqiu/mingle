package io.github.amings.mingle.svc.handler.impl;

import io.github.amings.mingle.svc.annotation.CodeDesc;
import io.github.amings.mingle.svc.annotation.MingleSvcMsg;
import io.github.amings.mingle.svc.handler.SvcMsgListHandler;
import io.github.amings.mingle.svc.handler.model.MsgModel;
import io.github.amings.mingle.svc.utils.CodeFiled;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link SvcMsgListHandler} impl
 *
 * @author Ming
 */
@MingleSvcMsg
public class SvcMsgListHandlerDefaultImpl implements SvcMsgListHandler {

    @Autowired
    List<CodeFiled> codeFiledList;

    @Override
    public List<MsgModel> getMsgList() {
        List<MsgModel> msgModels = new ArrayList<>();
        codeFiledList.forEach(node -> {
            for (Field field : node.getClass().getFields()) {
                msgModels.add(getMsgModel(field.getName(), field.getAnnotation(CodeDesc.class).value()));
            }
        });
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
