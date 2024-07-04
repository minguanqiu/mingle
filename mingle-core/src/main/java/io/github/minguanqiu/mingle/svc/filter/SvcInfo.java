package io.github.minguanqiu.mingle.svc.filter;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.minguanqiu.mingle.svc.SvcResponseHeader;
import io.github.minguanqiu.mingle.svc.register.SvcDefinition;
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

  /**
   * Serial number for service logging.
   *
   * @return return the service serial number.
   */
  private String svcSerialNum;

  /**
   * Start date time for logging.
   *
   * @return return the start date time.
   */
  private LocalDateTime startDateTime;

  /**
   * End date time for logging.
   *
   * @return return the end date time.
   */
  private LocalDateTime endDateTime;

  /**
   * Http servlet request.
   *
   * @return return the http servlet request.
   */
  private HttpServletRequest httpServletRequest;

  /**
   * Http servlet response.
   *
   * @return return the http servlet response.
   */
  private HttpServletResponse httpServletResponse;

  /**
   * Service definition.
   *
   * @return return the service definition.
   */
  private SvcDefinition svcDefinition;

  /**
   * Service response header.
   *
   * @param svcResponseHeader the service response header.
   * @return return the service response header.
   */
  @Setter(AccessLevel.PUBLIC)
  private SvcResponseHeader svcResponseHeader;

  /**
   * Servlet request body.
   *
   * @return return the request body.
   */
  private String requestBody;

  /**
   * Service request.
   *
   * @return return the service request object.
   */
  private Object svcRequest;

  /**
   * Service response body.
   *
   * @return return the response body of json node.
   */
  private JsonNode responseBody;

  /**
   * Service response body.
   *
   * @return return the response body object.
   */
  private Object svcResponseBody;

}
