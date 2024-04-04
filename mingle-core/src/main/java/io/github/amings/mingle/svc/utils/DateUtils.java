package io.github.amings.mingle.svc.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * @author Ming
 */

public class DateUtils {

    public static LocalDateTime getNowLocalDateTime() {
        return LocalDateTime.now();
    }

    public static Optional<String> dateTimeFormat(LocalDateTime datetime, String format) {
        return datetime.format(DateTimeFormatter.ofPattern(format)).describeConstable();
    }

    public static long between(ChronoUnit chronoUnit, LocalDateTime temporal1Inclusive, LocalDateTime temporal2Exclusive) {
        return chronoUnit.between(temporal1Inclusive, temporal2Exclusive);
    }

}
