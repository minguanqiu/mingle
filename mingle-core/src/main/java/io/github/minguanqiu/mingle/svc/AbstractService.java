package io.github.minguanqiu.mingle.svc;

import com.google.common.reflect.TypeToken;
import io.github.minguanqiu.mingle.svc.annotation.Svc;
import io.github.minguanqiu.mingle.svc.exception.BreakSvcProcessException;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import lombok.Getter;

/**
 * {@inheritDoc}
 * This class provide service register must extend and add {@link Svc} on target class
 *
 * @author Ming
 */
public abstract non-sealed class AbstractService<Req extends SvcRequest, Res extends SvcResponseBody> implements Service<Req, Res> {

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
     * @throws BreakSvcProcessException will throw
     **/
    protected void throwLogic(SvcResponseHeader svcResponseHeader) throws BreakSvcProcessException {
        throwLogic(svcResponseHeader, null);
    }

    /**
     * Interrupt service logic
     *
     * @param svcResponseHeader service response header
     * @param svcResponse       service response
     * @throws BreakSvcProcessException will throw
     **/
    protected void throwLogic(SvcResponseHeader svcResponseHeader, Res svcResponse) throws BreakSvcProcessException {
        throw new BreakSvcProcessException(svcResponseHeader, svcResponse);
    }

    /**
     * Return service logic
     *
     * @param svcResponseHeader service response header
     **/
    protected Res returnLogic(SvcResponseHeader svcResponseHeader) {
        return returnLogic(svcResponseHeader, null);
    }

    /**
     * Return service logic
     *
     * @param svcResponseHeader service response header
     * @param svcResponse       service response
     **/
    protected Res returnLogic(SvcResponseHeader svcResponseHeader, Res svcResponse) {
        svcInfo.setSvcResponseHeader(svcResponseHeader);
        return svcResponse;
    }


}
