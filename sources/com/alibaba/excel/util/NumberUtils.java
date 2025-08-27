package com.alibaba.excel.util;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/util/NumberUtils.class */
public class NumberUtils {
    private NumberUtils() {
    }

    public static String format(Number num, ExcelContentProperty contentProperty) {
        if (contentProperty == null || contentProperty.getNumberFormatProperty() == null || StringUtils.isEmpty(contentProperty.getNumberFormatProperty().getFormat())) {
            if (num instanceof BigDecimal) {
                return ((BigDecimal) num).toPlainString();
            }
            return num.toString();
        }
        String format = contentProperty.getNumberFormatProperty().getFormat();
        RoundingMode roundingMode = contentProperty.getNumberFormatProperty().getRoundingMode();
        DecimalFormat decimalFormat = new DecimalFormat(format);
        decimalFormat.setRoundingMode(roundingMode);
        return decimalFormat.format(num);
    }

    public static CellData formatToCellData(Number num, ExcelContentProperty contentProperty) {
        return new CellData(format(num, contentProperty));
    }

    public static Short parseShort(String string, ExcelContentProperty contentProperty) throws ParseException {
        if (!hasFormat(contentProperty)) {
            return Short.valueOf(string);
        }
        return Short.valueOf(parse(string, contentProperty).shortValue());
    }

    public static Long parseLong(String string, ExcelContentProperty contentProperty) throws ParseException {
        if (!hasFormat(contentProperty)) {
            return Long.valueOf(string);
        }
        return Long.valueOf(parse(string, contentProperty).longValue());
    }

    public static Integer parseInteger(String string, ExcelContentProperty contentProperty) throws ParseException {
        if (!hasFormat(contentProperty)) {
            return Integer.valueOf(string);
        }
        return Integer.valueOf(parse(string, contentProperty).intValue());
    }

    public static Float parseFloat(String string, ExcelContentProperty contentProperty) throws ParseException {
        if (!hasFormat(contentProperty)) {
            return Float.valueOf(string);
        }
        return Float.valueOf(parse(string, contentProperty).floatValue());
    }

    public static BigDecimal parseBigDecimal(String string, ExcelContentProperty contentProperty) throws ParseException {
        if (!hasFormat(contentProperty)) {
            return new BigDecimal(string);
        }
        return BigDecimal.valueOf(parse(string, contentProperty).doubleValue());
    }

    public static Byte parseByte(String string, ExcelContentProperty contentProperty) throws ParseException {
        if (!hasFormat(contentProperty)) {
            return Byte.valueOf(string);
        }
        return Byte.valueOf(parse(string, contentProperty).byteValue());
    }

    public static Double parseDouble(String string, ExcelContentProperty contentProperty) throws ParseException {
        if (!hasFormat(contentProperty)) {
            return Double.valueOf(string);
        }
        return Double.valueOf(parse(string, contentProperty).doubleValue());
    }

    private static boolean hasFormat(ExcelContentProperty contentProperty) {
        return (contentProperty == null || contentProperty.getNumberFormatProperty() == null || StringUtils.isEmpty(contentProperty.getNumberFormatProperty().getFormat())) ? false : true;
    }

    private static Number parse(String string, ExcelContentProperty contentProperty) throws ParseException {
        String format = contentProperty.getNumberFormatProperty().getFormat();
        RoundingMode roundingMode = contentProperty.getNumberFormatProperty().getRoundingMode();
        DecimalFormat decimalFormat = new DecimalFormat(format);
        decimalFormat.setRoundingMode(roundingMode);
        return decimalFormat.parse(string);
    }
}
