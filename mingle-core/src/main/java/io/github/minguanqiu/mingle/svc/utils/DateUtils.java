package io.github.minguanqiu.mingle.svc.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Utils for data process.
 *
 * @author Qiu Guan Ming
 */

public class DateUtils {

  /**
   * Get now local date time.
   *
   * @return return the local date time.
   */
  public static LocalDateTime getNowLocalDateTime() {
    return LocalDateTime.now();
  }

  /**
   * Format date time.
   *
   * @param datetime the local date time.
   * @param format   the date format pattern.
   * @return return the optional string for date.
   */
  public static Optional<String> dateTimeFormat(LocalDateTime datetime, String format) {
    return datetime.format(DateTimeFormatter.ofPattern(format)).describeConstable();
  }

  /**
   * Between date time.
   *
   * @param chronoUnit         the ChronoUnit.
   * @param temporal1Inclusive the local date time.
   * @param temporal2Exclusive the local date time.
   * @return return the time of gap.
   */
  public static long between(ChronoUnit chronoUnit, LocalDateTime temporal1Inclusive,
      LocalDateTime temporal2Exclusive) {
    return chronoUnit.between(temporal1Inclusive, temporal2Exclusive);
  }

}
