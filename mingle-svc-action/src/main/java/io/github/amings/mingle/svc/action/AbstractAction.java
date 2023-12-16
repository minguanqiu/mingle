package io.github.amings.mingle.svc.action;

import com.google.common.reflect.TypeToken;
import io.github.amings.mingle.svc.action.annotation.AutoBreak;
import io.github.amings.mingle.svc.action.exception.ActionAutoBreakException;
import io.github.amings.mingle.svc.action.exception.BreakActionLogicException;
import io.github.amings.mingle.svc.action.exception.resolver.ActionExceptionHandlerResolver;
import io.github.amings.mingle.utils.ReflectionUtils;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;


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

public abstract class AbstractAction<Req extends ActionReqModel, Res extends ActionResModel, ReqData extends ActionReqData, ResData extends ActionResData<Res>> {

    public static final String ACTION_SUCCESS_CODE = "0";

    public static final String ACTION_SUCCESS_MSG = "successful";

    @Value("${mingle.svc.action.autoBreak:false}")
    private boolean globalAutoBreak;
    @Autowired
    ActionExceptionHandlerResolver actionExceptionHandlerResolver;
    @Getter(AccessLevel.PROTECTED)
    private final Class<ReqData> reqDataClass;
    @Getter(AccessLevel.PROTECTED)
    private final Class<ResData> resDataClass;
    @Getter(AccessLevel.PROTECTED)
    private final Class<Res> resModelClass;

    /**
     * constructor
     */
    @SuppressWarnings("unchecked")
    public AbstractAction() {
        TypeToken<ResData> resDataTypeToken = new TypeToken<ResData>((getClass())) {
        };
        TypeToken<ReqData> reqDataTypeToken = new TypeToken<ReqData>((getClass())) {
        };
        TypeToken<Res> resModelTypeToken = new TypeToken<Res>((getClass())) {
        };
        reqDataClass = (Class<ReqData>) reqDataTypeToken.getRawType();
        resDataClass = (Class<ResData>) resDataTypeToken.getRawType();
        resModelClass = (Class<Res>) resModelTypeToken.getRawType();
    }

    /**
     * Execute Action logic
     *
     * @param reqModel Action request model
     * @return ResData
     */
    public ResData doAction(Req reqModel) {
        return doAction(reqModel, ReflectionUtils.newInstance(reqDataClass));
    }

    /**
     * Execute Action logic
     *
     * @param reqModel Action request model
     * @param reqData  Action request data
     * @return ResData
     */
    public ResData doAction(Req reqModel, ReqData reqData) {
        ResData resData = ReflectionUtils.newInstance(resDataClass);
        Objects.requireNonNull(resData, "request data newInstance fail");
        try {
            resData.setCode(ACTION_SUCCESS_CODE);
            resData.setDesc(ACTION_SUCCESS_MSG);
            resData.setResModel(processAction(reqModel, reqData, resData));
        } catch (BreakActionLogicException e1) {
            resData.setCode(e1.getCode());
            resData.setDesc(e1.getDesc());
            if(e1.getResModel() != null) {
                resData.setResModel((Res) e1.getResModel());
            }
        } catch (Exception e) {
            ActionExceptionModel actionExceptionModel = actionExceptionHandlerResolver.resolver(e);
            resData.setCode(actionExceptionModel.getCode());
            resData.setDesc(actionExceptionModel.getDesc());
        }
        resData.setMsgType(getMsgType());
        checkSuccess(reqData.getAutoBreak(), resData);
        return resData;
    }

    /**
     * Process action logic
     *
     * @param reqModel Action request model
     * @param reqData  Action request data
     * @param resData  Action response data
     * @return Res
     */
    protected abstract Res processAction(Req reqModel, ReqData reqData, ResData resData);

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
        return "action";
    }

    /**
     * Check action is success and set status
     *
     * @param autoBreak     autoBreak
     * @param actionResData action response data
     */
    private void checkSuccess(AutoBreak autoBreak, ResData actionResData) {
        if (!ACTION_SUCCESS_CODE.equals(actionResData.getCode())) {
            switch (autoBreak) {
                case GLOBAL:
                    if (globalAutoBreak) {
                        throw new ActionAutoBreakException(actionResData);
                    }
                    break;
                case TURE:
                    throw new ActionAutoBreakException(actionResData);
            }
        } else {
            actionResData.setSuccess(true);
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

}
