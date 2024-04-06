package io.github.minguanq.mingle.svc.filter;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.minguanq.mingle.svc.SvcResponseHeader;
import io.github.minguanq.mingle.svc.component.SvcRegisterComponent;
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

    private SvcRegisterComponent.SvcDefinition svcDefinition;

    @Setter(AccessLevel.PUBLIC)
    private SvcResponseHeader svcResponseHeader;

    private String requestBody;

    private Object svcRequest;

    private JsonNode responseBody;

    private Object svcResponseBody;

}
