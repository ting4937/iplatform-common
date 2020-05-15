package cn.hollycloud.iplatform.common.utils;


import cn.hollycloud.iplatform.common.constant.ValueConstant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class DateUtil {
    private final static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(ValueConstant.DEFAULT_DATE_FORMAT);
    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ValueConstant.DEFAULT_DATE_TIME_FORMAT);

    public static String formatDate(LocalDate date) {
        return date.format(dateFormatter);
    }

    public static String formatDate(LocalDateTime date) {
        return date.format(dateFormatter);
    }

    public static String formatDateTime(LocalDate date) {
        return date.format(dateTimeFormatter);
    }

    public static String formatDateTime(LocalDateTime date) {
        return date.format(dateTimeFormatter);
    }

    public static LocalDate parseDate(String date) {
        return LocalDate.parse(date, dateFormatter);
    }

    public static LocalDateTime parseDateTime(String datetime) {
        return LocalDateTime.parse(datetime, dateTimeFormatter);
    }
}
