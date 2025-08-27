package com.alibaba.excel.util;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/util/BooleanUtils.class */
public class BooleanUtils {
    private static final String TRUE_NUMBER = "1";

    private BooleanUtils() {
    }

    public static Boolean valueOf(String str) {
        if ("1".equals(str)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
