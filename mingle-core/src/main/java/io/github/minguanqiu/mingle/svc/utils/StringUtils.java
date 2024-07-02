package io.github.minguanqiu.mingle.svc.utils;

import java.util.Map;
import org.apache.commons.text.StringSubstitutor;

/**
 * @author Qiu Guan Ming
 */

public class StringUtils {

  public static String templateConvert(String template, Map<String, String> valueMap, String prefix,
      String suffix) {
    return StringSubstitutor.replace(template, valueMap, prefix, suffix);
  }

}
