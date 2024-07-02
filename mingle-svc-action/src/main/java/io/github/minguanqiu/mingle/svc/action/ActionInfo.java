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

  private String svcSerialNum;

  private String actSerialNum;

  private LocalDateTime startDateTime = LocalDateTime.now();

  private Class<?> actionClass;

  private ActionRequest actionRequest;

  private ActionResponseBody actionResponseBody;

  @Setter
  private String code;

  @Setter
  private String msg;

  private Map<String, Object> values = new HashMap<>();

}
