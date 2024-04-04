package io.github.amings.mingle.svc.filter;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.amings.mingle.svc.SvcResponseHeader;
import io.github.amings.mingle.svc.component.SvcBinderComponent;
import io.github.amings.mingle.svc.exception.SvcReqModelValidFailException;
import io.github.amings.mingle.svc.handler.SvcResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDateTime;

/**
 * This class is a spring request scope bean,will keep service need information
 *
 * @author Ming
 */

@Getter
@Setter(AccessLevel.PACKAGE)
@Component
@RequestScope
public class SvcInfo {

    private String svcSerialNum;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private HttpServletRequest httpServletRequest;

    private HttpServletResponse httpServletResponse;

    private SvcBinderComponent.SvcBinderModel svcBinderModel;

    private SvcResponseHeader svcResponseHeader;

    private SvcResponseHeader svcResponseHeaderLog;

    private SvcResponseHandler svcResponseHandler;

    private String payLoadString;

    private JsonNode payLoadNode;

    private JsonNode responseBody;

    private SvcReqModelValidFailException svcReqModelValidFailException;

    private Object svcRequest;

}
