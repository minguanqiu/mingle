package io.github.minguanqiu.mingle.svc.filter;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.minguanqiu.mingle.svc.SvcResponseHeader;
import io.github.minguanqiu.mingle.svc.register.SvcRegister;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * A spring request scope bean,will keep service information
 *
 * @author Qiu Guan Ming
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

  private SvcRegister.SvcDefinition svcDefinition;

  @Setter(AccessLevel.PUBLIC)
  private SvcResponseHeader svcResponseHeader;

  private String requestBody;

  private Object svcRequest;

  private JsonNode responseBody;

  private Object svcResponseBody;

}
