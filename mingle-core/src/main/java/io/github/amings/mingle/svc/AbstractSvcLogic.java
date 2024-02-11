package io.github.amings.mingle.svc;

import io.github.amings.mingle.svc.action.ActionResData;
import io.github.amings.mingle.svc.configuration.properties.SvcProperties;
import io.github.amings.mingle.svc.exception.BreakSvcProcessException;
import io.github.amings.mingle.svc.filter.SvcInfo;
import io.github.amings.mingle.svc.handler.SvcMsgHandler;

import java.util.Map;

/**
 * Base Svc class for All Svc logic，must be implements will get Svc Feature
 *
 * @author Ming
 */

public abstract class AbstractSvcLogic<Req extends SvcReqModel, Res extends SvcResModel> {

    protected final SvcInfo svcInfo;
    protected final SvcMsgHandler svcMsgHandler;
    protected final SvcProperties svcProperties;


    public AbstractSvcLogic(SvcInfo svcInfo, SvcMsgHandler svcMsgHandler, SvcProperties svcProperties) {
        this.svcInfo = svcInfo;
        this.svcMsgHandler = svcMsgHandler;
        this.svcProperties = svcProperties;
    }

    public abstract Res doService(Req reqModel, Res resModel);

    /**
     * @param code response code
     *             interrupt Svc Logic by throw exception
     **/
    protected void breakSvcLogic(String code) throws BreakSvcProcessException {
        breakSvcLogic(code, svcMsgHandler.getMsg(svcProperties.getMsgType(), code));
    }

    /**
     * @param code   response code
     * @param values template convert values
     *               interrupt Svc Logic by throw exception
     **/
    protected void breakSvcLogic(String code, Map<String, String> values) throws BreakSvcProcessException {
        breakSvcLogic(code, svcMsgHandler.getMsg(svcProperties.getMsgType(), code, values));
    }

    /**
     * @param actionResData Action response data
     *                      interrupt Svc Logic by throw exception
     **/
    protected void breakSvcLogic(ActionResData<?> actionResData) throws BreakSvcProcessException {
        breakSvcLogic(actionResData.getCode(), svcMsgHandler.getMsg(actionResData.getMsgType(), actionResData.getCode()));
    }

    /**
     * @param code response code
     * @param desc response desc
     *             interrupt Svc Logic by throw exception
     **/
    protected void breakSvcLogic(String code, String desc) throws BreakSvcProcessException {
        breakSvcLogic(code, desc, null);
    }

    /**
     * @param code     response code
     * @param resModel Svc response model
     *                 interrupt Svc Logic by throw exception
     **/
    protected void breakSvcLogic(String code, Res resModel) throws BreakSvcProcessException {
        breakSvcLogic(code, svcMsgHandler.getMsg(svcProperties.getMsgType(), code), resModel);
    }

    /**
     * @param code     response code
     * @param desc     response desc
     * @param resModel Svc response model
     *                 interrupt Svc Logic by throw exception
     **/
    protected void breakSvcLogic(String code, String desc, Res resModel) throws BreakSvcProcessException {
        throw new BreakSvcProcessException(code, desc, resModel);
    }

    /**
     * @param code response code
     * @return Res Svc response model
     * interrupt Svc Logic by return
     **/
    protected Res returnSvcLogic(String code) {
        return returnSvcLogic(code, svcMsgHandler.getMsg(svcProperties.getMsgType(), code), null);
    }

    /**
     * @param code   response code
     * @param values template convert values
     * @return Res Svc response model
     * interrupt Svc Logic by return
     **/
    protected Res returnSvcLogic(String code, Map<String, String> values) {
        return returnSvcLogic(code, svcMsgHandler.getMsg(svcProperties.getMsgType(), code, values), null);
    }

    /**
     * @param actionResData Action response data
     * @return Res Svc response model
     * interrupt Svc Logic by return
     **/
    protected Res returnSvcLogic(ActionResData<?> actionResData) {
        return returnSvcLogic(actionResData.getCode(), actionResData.getDesc(), null);
    }

    /**
     * @param code response code
     * @param desc response desc
     * @return Res Svc response model
     * interrupt Svc Logic by return
     **/
    protected Res returnSvcLogic(String code, String desc) {
        return returnSvcLogic(code, desc, null);
    }

    /**
     * @param code     response code
     * @param resModel Svc response model
     * @return Res Svc response model
     * interrupt Svc Logic by return
     **/
    protected Res returnSvcLogic(String code, Res resModel) {
        return returnSvcLogic(code, svcMsgHandler.getMsg(svcProperties.getMsgType(), code), resModel);
    }

    /**
     * @param code     response code
     * @param desc     response desc
     * @param resModel Svc response model
     * @return Res Svc response model
     * interrupt Svc Logic by return
     **/
    protected Res returnSvcLogic(String code, String desc, Res resModel) {
        svcInfo.setCode(code);
        svcInfo.setDesc(desc);
        return resModel;
    }

}
