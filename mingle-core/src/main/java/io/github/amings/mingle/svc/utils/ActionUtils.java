package io.github.amings.mingle.svc.utils;

import io.github.amings.mingle.svc.action.AbstractAction;
import io.github.amings.mingle.svc.action.ActionReqData;
import io.github.amings.mingle.svc.action.ActionReqModel;
import io.github.amings.mingle.svc.action.ActionResData;
import io.github.amings.mingle.svc.action.ActionResModel;
import io.github.amings.mingle.svc.log.SvcLogModel;
import io.github.amings.mingle.svc.log.SvcLogThreadLocal;

/**
 * @author Ming
 */
public class ActionUtils {

    /**
     * using single thread or not request scope
     */
    public static <Req extends ActionReqModel, Res extends ActionResModel, ReqData extends ActionReqData, ResData extends ActionResData<Res>> ResData doActionByLog(SvcLogModel svcLogModel,AbstractAction<Req, Res, ReqData, ResData> action, Req reqModel) {
        SvcLogThreadLocal.set(svcLogModel);
        ResData resData = action.doAction(reqModel);
        SvcLogThreadLocal.remove();
        return resData;
    }

}
