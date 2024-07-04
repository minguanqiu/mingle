package io.github.minguanqiu.mingle.svc.action;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * This class provide when execute action process logic can controller result
 *
 * @author Qiu Guan Ming
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class ActionInfo {

  /**
   * Serial number for service logging.
   *
   * @param svcSerialNum the serial number for service.
   * @return return the serial number for service.
   */
  private String svcSerialNum;

  /**
   * Serial number for action logging.
   *
   * @param actSerialNum the serial number for action.
   * @return return the serial number for action.
   */
  private String actSerialNum;

  /**
   * Action execute start date time for logging.
   *
   * @param startDateTime the start date time.
   * @return return the start date time.
   */
  private LocalDateTime startDateTime = LocalDateTime.now();

  /**
   * Action object class.
   *
   * @param actionClass the action object class.
   * @return return the action object class.
   */
  private Class<?> actionClass;

  /**
   * Action request.
   *
   * @param actionRequest the action request.
   * @return return the action request.
   */
  private ActionRequest actionRequest;

  /**
   * Action response body.
   *
   * @param actionResponseBody the action response body.
   * @return return the action response body.
   */
  private ActionResponseBody actionResponseBody;

  /**
   * Action response code.
   *
   * @param code the action response code.
   * @return return the action response code.
   */
  @Setter
  private String code;

  /**
   * Action response message.
   *
   * @param msg the action response message.
   * @return return the action response message.
   */
  @Setter
  private String msg;

  /**
   * Action process tmp map.
   *
   * @param values the action process map.
   * @return return the action process tmp map.
   */
  private Map<String, Object> values = new HashMap<>();

}
