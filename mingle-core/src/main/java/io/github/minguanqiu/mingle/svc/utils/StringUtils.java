package io.github.minguanqiu.mingle.svc.utils;

import java.util.Map;
import org.apache.commons.text.StringSubstitutor;

/**
 * Utils for string process.
 *
 * @author Qiu Guan Ming
 */

public class StringUtils {

  /**
   * Template convert for string.
   *
   * @param template the origin string.
   * @param valueMap the mapping map.
   * @param prefix   the prefix.
   * @param suffix   the suffix.
   * @return return the format string.
   */
  public static String templateConvert(String template, Map<String, String> valueMap, String prefix,
      String suffix) {
    return StringSubstitutor.replace(template, valueMap, prefix, suffix);
  }

}
