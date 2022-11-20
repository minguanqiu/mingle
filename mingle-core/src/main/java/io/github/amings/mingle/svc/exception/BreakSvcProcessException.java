package io.github.amings.mingle.svc.exception;

import io.github.amings.mingle.svc.SvcResModel;
import lombok.Getter;

/**
 * for Svc break logic Exception
 *
 * @author Ming
 */

@Getter
public class BreakSvcProcessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String code;

    private final String desc;

    private SvcResModel svcResModel = new SvcResModel();

    /**
     * @param code        response code
     * @param desc        response msg
     * @param svcResModel Svc response model
     */
    public BreakSvcProcessException(String code, String desc, SvcResModel svcResModel) {
        this.code = code;
        this.desc = desc;
        if(svcResModel != null) {
            this.svcResModel = svcResModel;
        }
    }

}
