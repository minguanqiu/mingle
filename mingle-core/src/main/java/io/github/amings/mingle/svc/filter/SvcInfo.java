package io.github.amings.mingle.svc.filter;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.amings.mingle.svc.SvcReqModel;
import io.github.amings.mingle.svc.component.SvcBinderComponent;
import io.github.amings.mingle.svc.exception.SvcReqModelValidFailException;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import io.github.amings.mingle.svc.utils.SvcSuccessField;
import io.github.amings.mingle.utils.DateUtils;
import io.github.amings.mingle.utils.UUIDUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * request scope bean for Svc request，will save Svc data
 *
 * @author Ming
 */

@Data
@Component
@RequestScope
public class SvcInfo {

    @Getter
    private final String uuid = UUIDUtils.generateUuidRandom();

    @Getter
    private final LocalDateTime startDateTime = DateUtils.getNowLocalDateTime();

    @Setter(AccessLevel.PACKAGE)
    private HttpServletRequest httpServletRequest;

    @Setter(AccessLevel.PACKAGE)
    private HttpServletResponse httpServletResponse;

    @Setter(AccessLevel.PACKAGE)
    private SvcBinderComponent.SvcBinderModel svcBinderModel;

    @Setter(AccessLevel.PACKAGE)
    private String svcUuid;

    @Setter(AccessLevel.PACKAGE)
    private String svcName;

    @Setter(AccessLevel.PACKAGE)
    private String payLoadString;

    @Setter(AccessLevel.PACKAGE)
    private JsonNode payLoadNode;

    private String code = SvcSuccessField.getSuccessCode();

    private String desc = SvcSuccessField.getSuccessDesc();

    @Setter(AccessLevel.PACKAGE)
    private String ip;

    @Setter(AccessLevel.PACKAGE)
    private boolean reqModelNeedValid = false;

    @Setter(AccessLevel.PACKAGE)
    private SvcReqModelValidFailException svcReqModelValidFailException;

    @Setter(AccessLevel.PACKAGE)
    private Object validReqModel;

    @Setter(AccessLevel.PACKAGE)
    private boolean writeBackReq = false;

    private SvcReqModel backReqModel;

    private SvcResModelHandler svcResModelHandler4Log;

    @Setter(AccessLevel.PACKAGE)
    private boolean exception = false;

}
