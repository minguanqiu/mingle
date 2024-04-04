package io.github.amings.mingle.svc;

import com.google.common.reflect.TypeToken;
import io.github.amings.mingle.svc.exception.BreakSvcProcessException;
import io.github.amings.mingle.svc.filter.SvcInfo;
import lombok.Getter;

/**
 * {@inheritDoc}
 * This class provide service register must extend and add {@link io.github.amings.mingle.svc.annotation.Svc} on target class
 *
 * @author Ming
 */

public abstract non-sealed class AbstractService<Req extends SvcRequest, Res extends SvcResponse> implements Service<Req, Res> {

    protected final SvcInfo svcInfo;

    @Getter
    protected final Class<Req> reqClass;

    @Getter
    protected final Class<Res> resClass;


    @SuppressWarnings("unchecked")
    public AbstractService(SvcInfo svcInfo) {
        this.svcInfo = svcInfo;
        reqClass = (Class<Req>) new TypeToken<Req>(getClass()) {
        }.getRawType();
        resClass = (Class<Res>) new TypeToken<Res>(getClass()) {
        }.getRawType();
    }

    /**
     * Interrupt service logic
     *
     * @param svcResponseHeader service response header
     * @param svcResponse       service response
     * @throws BreakSvcProcessException if call this method
     **/
    protected void breakSvcLogic(SvcResponseHeader svcResponseHeader, Res svcResponse) throws BreakSvcProcessException {
        SvcResponseHeader svcInfoSvcResponseHeader = svcInfo.getSvcResponseHeader();
        svcInfoSvcResponseHeader.setCode(svcResponseHeader.getCode());
        svcInfoSvcResponseHeader.setMsg(svcResponseHeader.getMsg());
        svcInfoSvcResponseHeader.setConvertMap(svcResponseHeader.getConvertMap());
        throw new BreakSvcProcessException(svcResponseHeader.getCode(), svcResponseHeader.getMsg(), svcResponse);
    }

    /**
     * Return service logic
     *
     * @param svcResponseHeader service response header
     * @param svcResponse       service response
     **/
    protected Res returnSvcLogic(SvcResponseHeader svcResponseHeader, Res svcResponse) {
        SvcResponseHeader svcInfoSvcResponseHeader = svcInfo.getSvcResponseHeader();
        svcInfoSvcResponseHeader.setCode(svcResponseHeader.getCode());
        svcInfoSvcResponseHeader.setMsg(svcResponseHeader.getMsg());
        svcInfoSvcResponseHeader.setConvertMap(svcResponseHeader.getConvertMap());
        return svcResponse;
    }


}
