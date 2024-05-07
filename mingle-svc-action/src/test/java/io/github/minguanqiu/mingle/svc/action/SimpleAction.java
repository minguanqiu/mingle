package io.github.minguanqiu.mingle.svc.action;

import io.github.minguanqiu.mingle.svc.action.configuration.properties.SvcActionProperties;
import io.github.minguanqiu.mingle.svc.action.exception.resolver.ActionExceptionHandlerResolver;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ming
 */
@Service
public class SimpleAction extends AbstractAction<SimpleActionReq, SimpleActionRes> {

    public SimpleAction(SvcActionProperties svcActionProperties, ActionExceptionHandlerResolver actionExceptionHandlerResolver, List<ActionInterceptor> actionInterceptors) {
        super(svcActionProperties, actionExceptionHandlerResolver, actionInterceptors);
    }

    @Override
    protected SimpleActionRes processLogic(SimpleActionReq request, ActionInfo actionInfo) {
        if(request.getText1().isEmpty()) {
            actionInfo.setCode("X001");
            actionInfo.setMsg("Text1 is empty");
            return null;
        }
        SimpleActionRes simpleActionRes = new SimpleActionRes();
        simpleActionRes.setText1(request.getText1());
        simpleActionRes.setText2(request.getText2());
        return simpleActionRes;
    }


    @Override
    public String getType() {
        return "simple";
    }
}
