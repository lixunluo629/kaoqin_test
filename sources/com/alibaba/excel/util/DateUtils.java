package com.alibaba.excel.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/util/DateUtils.class */
public class DateUtils {
    public static final String DATE_FORMAT_10 = "yyyy-MM-dd";
    public static final String DATE_FORMAT_14 = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT_17 = "yyyyMMdd HH:mm:ss";
    public static final String DATE_FORMAT_19 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_19_FORWARD_SLASH = "yyyy/MM/dd HH:mm:ss";
    private static final String MINUS = "-";

    private DateUtils() {
    }

    public static Date parseDate(String dateString, String dateFormat) throws ParseException {
        if (StringUtils.isEmpty(dateFormat)) {
            dateFormat = switchDateFormat(dateString);
        }
        return new SimpleDateFormat(dateFormat).parse(dateString);
    }

    public static Date parseDate(String dateString) throws ParseException {
        return parseDate(dateString, switchDateFormat(dateString));
    }

    private static String switchDateFormat(String dateString) {
        int length = dateString.length();
        switch (length) {
            case 10:
                return "yyyy-MM-dd";
            case 11:
            case 12:
            case 13:
            case 15:
            case 16:
            case 18:
            default:
                throw new IllegalArgumentException("can not find date format for：" + dateString);
            case 14:
                return "yyyyMMddHHmmss";
            case 17:
                return DATE_FORMAT_17;
            case 19:
                if (dateString.contains("-")) {
                    return "yyyy-MM-dd HH:mm:ss";
                }
                return DATE_FORMAT_19_FORWARD_SLASH;
        }
    }

    public static String format(Date date) {
        return format(date, null);
    }

    public static String format(Date date, String dateFormat) {
        if (date == null) {
            return "";
        }
        if (StringUtils.isEmpty(dateFormat)) {
            dateFormat = "yyyy-MM-dd HH:mm:ss";
        }
        return new SimpleDateFormat(dateFormat).format(date);
    }
}
