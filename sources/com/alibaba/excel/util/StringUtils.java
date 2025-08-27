package com.alibaba.excel.util;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/util/StringUtils.class */
public class StringUtils {
    public static final String EMPTY = "";

    private StringUtils() {
    }

    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }
}
