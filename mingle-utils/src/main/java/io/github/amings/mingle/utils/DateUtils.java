package io.github.amings.mingle.utils;

import io.github.amings.mingle.utils.enums.DateType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.Chronology;
import java.time.chrono.MinguoChronology;
import java.time.chrono.MinguoDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.DecimalStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;

/**
 * @author Ming
 */

public class DateUtils {

    public final static String DefaultDatePattern = "yyyy/MM/dd";

    public final static String DefaultDateTimePattern = "yyyy/MM/dd HH:mm:ss";

    public final static String DefaultDateTimePatternCh = "yyyy年MM月dd日 HH時mm分ss秒";

    public final static String DefaultTimePattern = "HH:mm:ss";

    public static LocalDate getNowLocalDate() {
        return LocalDate.now();
    }

    public static LocalDateTime getNowLocalDateTime() {
        return LocalDateTime.now();
    }

    public static String LocalDateFormat(LocalDate localDate, String pattern) {
        return localDate.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String LocalDateTimeFormat(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static String date2LocalDateTime(Date date, String format) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                .format(DateTimeFormatter.ofPattern(format));
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    public static YearMonth getYearMonth(String date, String pattern) {
        try {
            return YearMonth.parse(date, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String getYearMonthDate(String date, String pattern, String format) {
        try {
            return YearMonth.parse(date, DateTimeFormatter.ofPattern(pattern))
                    .format(DateTimeFormatter.ofPattern(format));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static LocalDate getDate(String date, String pattern) {
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static LocalDateTime getDateTime(String date, String pattern) {
        try {
            return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String getDefaultNowTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern(DefaultTimePattern));
    }

    public static String getDefaultNowDate(DateType datetype) {
        switch (datetype) {
            case AD:
                return LocalDate.now().format(DateTimeFormatter.ofPattern(DefaultDatePattern));
            case RC:
                return MinguoDate.now().format(DateTimeFormatter.ofPattern(DefaultDatePattern));
            default:
                return "";
        }
    }

    public static String getDefaultNowDateTime(DateType datetype) {
        switch (datetype) {
            case AD:
                return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DefaultDateTimePattern));
            case RC:
                return MinguoDate.from(LocalDate.now()).atTime(LocalTime.now())
                        .format(DateTimeFormatter.ofPattern(DefaultDateTimePattern));
            default:
                return "";
        }
    }

    public static String getDefaultNowDateTimeCh() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DefaultDateTimePatternCh));
    }

    public static String getNowDateTime(DateType datetype, String pattern) {
        switch (datetype) {
            case AD:
                return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
            case RC:
                return MinguoDate.from(LocalDate.now()).atTime(LocalTime.now())
                        .format(DateTimeFormatter.ofPattern(pattern));
            default:
                return "";
        }
    }

    public static boolean checkADDateEqual(String date, String pattern) {

        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern)).isEqual(LocalDate.now());
    }

    public static boolean checkADDateBefore(String date, String pattern) {

        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern)).isBefore(LocalDate.now());
    }

    public static boolean checkADDateAfter(String date, String pattern) {

        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern)).isAfter(LocalDate.now());
    }

    public static boolean checkADYearMonthBefore(String date, String pattern) {

        return YearMonth.parse(date, DateTimeFormatter.ofPattern(pattern)).isBefore(YearMonth.now());
    }

    public static boolean checkADYearMonthAfter(String date, String pattern) {

        return YearMonth.parse(date, DateTimeFormatter.ofPattern(pattern)).isAfter(YearMonth.now());
    }

    public static String getNowDate(DateType datetype, String pattern) {
        switch (datetype) {
            case AD:
                return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
            case RC:
                return MinguoDate.now().format(DateTimeFormatter.ofPattern(pattern));
            default:
                return "";
        }
    }

    public static String getNowFirstDayOfMonth(DateType datetype, String pattern) {
        LocalDate localdate = LocalDate.now();
        switch (datetype) {
            case AD:
                return LocalDate.MIN.withYear(localdate.getYear()).withMonth(localdate.getMonthValue())
                        .format(DateTimeFormatter.ofPattern(pattern));
            case RC:
                MinguoDate rcdate = MinguoDate.now();
                return MinguoDate
                        .of(rcdate.get(ChronoField.YEAR), rcdate.get(ChronoField.MONTH_OF_YEAR),
                                LocalDate.MIN.withYear(rcdate.get(ChronoField.YEAR))
                                        .withMonth(rcdate.get(ChronoField.MONTH_OF_YEAR)).getDayOfMonth())
                        .format(DateTimeFormatter.ofPattern(pattern));
            default:
                return "";
        }
    }

    public static String getNowLastDayOfMonth(DateType datetype, String pattern) {

        switch (datetype) {
            case AD:
                LocalDate addate = LocalDate.now();
                return LocalDate.MAX.withYear(addate.getYear()).withMonth(addate.getMonthValue())
                        .format(DateTimeFormatter.ofPattern(pattern));
            case RC:
                MinguoDate rcdate = MinguoDate.now();
                return MinguoDate
                        .of(rcdate.get(ChronoField.YEAR), rcdate.get(ChronoField.MONTH_OF_YEAR),
                                LocalDate.MAX.withYear(rcdate.get(ChronoField.YEAR))
                                        .withMonth(rcdate.get(ChronoField.MONTH_OF_YEAR)).getDayOfMonth())
                        .format(DateTimeFormatter.ofPattern(pattern));
            default:
                return "";
        }
    }

    public static String getFirstDayOfMonth(String date, String datePattern, String formatPattern) {
        try {
            YearMonth yearmonth = YearMonth.parse(date, DateTimeFormatter.ofPattern(datePattern));
            return LocalDate.MIN.withYear(yearmonth.getYear()).withMonth(yearmonth.getMonthValue())
                    .format(DateTimeFormatter.ofPattern(formatPattern));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String getLastDayOfMonth(String date, String datePattern, String formatPattern) {
        try {

            YearMonth yearmonth = YearMonth.parse(date, DateTimeFormatter.ofPattern(datePattern));
            return LocalDate.MAX.withYear(yearmonth.getYear()).withMonth(yearmonth.getMonthValue())
                    .format(DateTimeFormatter.ofPattern(formatPattern));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String AdDateFormat(String date, String datePattern, String format) {
        try {
            LocalDate datetime = LocalDate.parse(date, DateTimeFormatter.ofPattern(datePattern));
            return datetime.format(DateTimeFormatter.ofPattern(format));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String AdDateTimeFormat(String date, String datePattern, String format) {
        try {
            LocalDateTime datetime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(datePattern));
            return datetime.format(DateTimeFormatter.ofPattern(format));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String AdDateTimeFormat(LocalDateTime datetime, String format) {
        try {
            return datetime.format(DateTimeFormatter.ofPattern(format));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String AdDateTimeFormatWithMill(String date, String datePattern, String format) {
        try {
            LocalDateTime datetime = LocalDateTime.parse(date, new DateTimeFormatterBuilder().appendPattern(datePattern)
                    .appendValue(ChronoField.MILLI_OF_SECOND, 3).toFormatter());
            return datetime.format(DateTimeFormatter.ofPattern(format));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String AdtoRcYearMonth(String date, String datePattern) {
        try {
            YearMonth yearMonth = YearMonth.parse(date, DateTimeFormatter.ofPattern(datePattern));
            return MinguoDate.from(LocalDate.now().withYear(yearMonth.getYear()).withMonth(yearMonth.getMonthValue()))
                    .format(DateTimeFormatter.ofPattern(datePattern));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String AdtoRcYearMonthDay(String date, String datePattern, String format) {
        try {
            LocalDate localdate = LocalDate.parse(date, DateTimeFormatter.ofPattern(datePattern));
            return MinguoDate.from(localdate).format(DateTimeFormatter.ofPattern(format));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String AdtoRcYearMonth(String date, String datePattern, String formatPattern) {
        try {
            YearMonth yearMonth = YearMonth.parse(date, DateTimeFormatter.ofPattern(datePattern));
            return MinguoDate.from(LocalDate.now().withYear(yearMonth.getYear()).withMonth(yearMonth.getMonthValue()))
                    .format(DateTimeFormatter.ofPattern(formatPattern));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String AdtoRcDateTime(String date, String datePattern, String formatPattern) {
        MinguoDate minguoDate = MinguoDate.now();
        MinguoChronology minguoChronology = minguoDate.getChronology();
        LocalDateTime localdatetime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(datePattern));
        ChronoLocalDateTime<MinguoDate> chronoLocalDateTime = minguoChronology.localDateTime(localdatetime);
        return chronoLocalDateTime.format(DateTimeFormatter.ofPattern(formatPattern));
    }

    public static String RcDateFormat(String date, String datePattern, String formatPattern) {
        Locale locale = Locale.getDefault(Locale.Category.FORMAT);
        Chronology chronology = MinguoChronology.INSTANCE;
        DateTimeFormatter df = new DateTimeFormatterBuilder().parseLenient().appendPattern(datePattern)
                .toFormatter().withChronology(chronology).withDecimalStyle(DecimalStyle.of(locale));
        TemporalAccessor temporal = df.parse(date);
        return chronology.date(temporal).format(DateTimeFormatter.ofPattern(formatPattern));
    }

    public static String RctoAdDate(String date, String datePattern, String formatPattern) {

        try {
            Locale locale = Locale.getDefault(Locale.Category.FORMAT);
            Chronology chronology = MinguoChronology.INSTANCE;
            DateTimeFormatter df = new DateTimeFormatterBuilder().parseLenient().appendPattern(datePattern)
                    .toFormatter().withChronology(chronology).withDecimalStyle(DecimalStyle.of(locale));
            TemporalAccessor temporal = df.parse(date);
            ChronoLocalDate cDate = chronology.date(temporal);
            if (datePattern.indexOf("HH") > 0 && formatPattern.indexOf("HH") > 0)
                return LocalDate.from(cDate).atTime(LocalTime.from(temporal))
                        .format(DateTimeFormatter.ofPattern(formatPattern));
            else
                return LocalDate.from(cDate).format(DateTimeFormatter.ofPattern(formatPattern));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static boolean LimitOneMonth(String beginDate, String endDate) {
        LocalDate blocaldate = LocalDate.parse(beginDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate elocaldate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
        if (blocaldate.plusMonths(1).isAfter(elocaldate) || blocaldate.plusMonths(1).isEqual(elocaldate)) {
            return true;
        }
        return false;
    }

    public static boolean checkPreDate(String date, boolean checktoday) { // 預約日期檢核&未來1個月
        LocalDate tmpdate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
        if (tmpdate == null || LocalDate.now().isAfter(tmpdate) || LocalDate.now().plusMonths(1).isBefore(tmpdate)) {
            return true;
        }
        if (checktoday) {
            if (LocalDate.now().isEqual(tmpdate)) {
                return true;
            }
        }
        return false;
    }

    public static long between(ChronoUnit chronoUnit, LocalDateTime temporal1Inclusive, LocalDateTime temporal2Exclusive) {
        return chronoUnit.between(temporal1Inclusive, temporal2Exclusive);
    }

    public static String getPreEndDate(String pattern) {
        return LocalDate.now().plusMonths(1).format(DateTimeFormatter.ofPattern(pattern));
    }

}
