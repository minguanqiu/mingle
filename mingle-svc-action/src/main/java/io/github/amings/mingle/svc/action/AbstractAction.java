package io.github.amings.mingle.svc.action;

import com.google.common.reflect.TypeToken;
import io.github.amings.mingle.svc.action.configuration.properties.ActionProperties;
import io.github.amings.mingle.svc.action.enums.AutoBreak;
import io.github.amings.mingle.svc.action.exception.ActionAutoBreakException;
import io.github.amings.mingle.svc.action.exception.BreakActionLogicException;
import io.github.amings.mingle.svc.action.exception.handler.model.ActionExceptionModel;
import io.github.amings.mingle.svc.action.exception.resolver.ActionExceptionHandlerResolver;
import io.github.amings.mingle.svc.action.interceptor.ActionInterceptor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * <pre>
 * Base Action class for all Action logic
 *
 * Generic:
 * Req - Action request model (like body)
 * Res - Action response model (like body)
 * ReqData - Action Request data (like header)
 * ResData - Action Response data (like header)
 * </pre>
 *
 * @author Ming
 */

public abstract class AbstractAction<Req extends ActionReqModel, ResData extends ActionResData, Res extends ActionResModel> {

    protected final ActionProperties actionProperties;
    private final ActionExceptionHandlerResolver actionExceptionHandlerResolver;
    private final List<ActionInterceptor> actionInterceptors;
    private Class<ResData> resDataClass;

    public AbstractAction(ActionProperties actionProperties, ActionExceptionHandlerResolver actionExceptionHandlerResolver, List<ActionInterceptor> actionInterceptors) {
        this.actionProperties = actionProperties;
        this.actionExceptionHandlerResolver = actionExceptionHandlerResolver;
        this.actionInterceptors = actionInterceptors;
        init();
    }

    /**
     * Execute Action logic
     *
     * @param reqModel Action request model
     * @return ResData
     */
    public ActionResponse<ResData, Res> doAction(Req reqModel) {
        List<ActionInterceptor> interceptors = processInterceptorBefore(reqModel);
        ActionResponse<ResData, Res> actionResponse = new ActionResponse<>();
        try {
            actionResponse.setCode(actionProperties.getSuccessCode());
            actionResponse.setDesc(actionProperties.getSuccessDesc());
            ResData resData = initResData();
            actionResponse.setResData(resData);
            actionResponse.setResModel(processLogic(reqModel, resData));
        } catch (BreakActionLogicException e1) {
            actionResponse.setCode(e1.getCode());
            actionResponse.setDesc(e1.getDesc());
            if (e1.getResModel() != null) {
                actionResponse.setResModel((Res) e1.getResModel());
            }
        } catch (Exception e) {
            if (actionExceptionHandlerResolver == null) {
                throw e;
            }
            ActionExceptionModel actionExceptionModel = actionExceptionHandlerResolver.resolver(e);
            actionResponse.setCode(actionExceptionModel.getCode());
            actionResponse.setDesc(actionExceptionModel.getDesc());
        }
        actionResponse.setMsgType(getMsgType());
        checkSuccess(reqModel.getAutoBreak(), actionResponse);
        processInterceptorAfter(interceptors, actionResponse);
        return actionResponse;
    }

    private List<ActionInterceptor> processInterceptorBefore(Req reqModel) {
        ArrayList<ActionInterceptor> interceptors = new ArrayList<>();
        actionInterceptors.forEach(actionInterceptor -> {
            if (actionInterceptor.supported(this)) {
                interceptors.add(actionInterceptor);
                actionInterceptor.before(this, reqModel);
            }
        });
        return interceptors;
    }

    private void processInterceptorAfter(List<ActionInterceptor> actionInterceptors, ActionResponse<ResData, Res> actionResponse) {
        actionInterceptors.forEach(actionInterceptor -> {
            actionInterceptor.after(this, actionResponse);
        });
    }

    protected ResData initResData() {
        try {
            return resDataClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Process action logic
     *
     * @param reqModel Action request model
     * @param resData
     * @return Res
     */
    protected abstract Res processLogic(Req reqModel, ResData resData);

    /**
     * Defined action type
     *
     * @return String
     */
    public abstract String getType();

    /**
     * Defined action msg type
     *
     * @return String
     */
    protected String getMsgType() {
        return actionProperties.getMsgType();
    }

    /**
     * Check action is success and set status
     *
     * @param autoBreak      autoBreak
     * @param actionResponse action response data
     */
    private void checkSuccess(AutoBreak autoBreak, ActionResponse<ResData, Res> actionResponse) {
        if (!actionProperties.getSuccessCode().equals(actionResponse.getCode())) {
            switch (autoBreak) {
                case GLOBAL:
                    if (actionProperties.isAutoBreak()) {
                        throw new ActionAutoBreakException(actionResponse);
                    }
                    break;
                case TURE:
                    throw new ActionAutoBreakException(actionResponse);
            }
        } else {
            actionResponse.setSuccess(true);
        }
    }

    /**
     * Break action logic
     *
     * @param code msg code
     * @param desc msg desc
     */
    protected void breakActionLogic(String code, String desc) {
        breakActionLogic(code, desc, null);
    }

    /**
     * Break action logic
     *
     * @param code     msg code
     * @param desc     msg desc
     * @param resModel Res
     */
    protected void breakActionLogic(String code, String desc, Res resModel) {
        throw new BreakActionLogicException(code, desc, resModel);
    }


    @SuppressWarnings("unchecked")
    private void init() {
        resDataClass = (Class<ResData>) new TypeToken<ResData>(getClass()) {
        }.getRawType();
    }

}
