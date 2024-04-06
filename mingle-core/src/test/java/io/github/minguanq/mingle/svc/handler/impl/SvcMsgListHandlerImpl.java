package io.github.minguanq.mingle.svc.handler.impl;

import io.github.minguanq.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanq.mingle.svc.handler.SvcMsgListHandler;
import io.github.minguanq.mingle.svc.handler.model.SvcMsgModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ming
 */
@Component
public class SvcMsgListHandlerImpl implements SvcMsgListHandler {

    private final SvcProperties svcProperties;
    
    public SvcMsgListHandlerImpl(SvcProperties svcProperties) {
        this.svcProperties = svcProperties;
    }

    @Override
    public List<SvcMsgModel> getMsgList() {
        ArrayList<SvcMsgModel> svcMsgModels = new ArrayList<>();
        svcMsgModels.add(svcMsgModel(svcProperties.getMsg_type(),"X001","convertX001 {var}"));
        svcMsgModels.add(svcMsgModel(svcProperties.getMsg_type(),"X002","convertX002 {var}"));
        svcMsgModels.add(svcMsgModel(svcProperties.getMsg_type(),"X003","convertX003"));
        svcMsgModels.add(svcMsgModel(svcProperties.getMsg_type(),"X004","convertX004"));
        svcMsgModels.add(svcMsgModel(svcProperties.getMsg_type(),"X005","convertX005"));
        return svcMsgModels;
    }

    public SvcMsgModel svcMsgModel(String msgType, String code, String msg) {
        return new SvcMsgModel(msgType, code, msg);
    }

}
