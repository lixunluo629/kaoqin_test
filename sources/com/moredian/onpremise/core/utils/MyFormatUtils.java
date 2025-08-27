package com.moredian.onpremise.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/MyFormatUtils.class */
public class MyFormatUtils {
    public static boolean isMobile(String mobile) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$", 2);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    public static boolean checkDeptName(String deptName) {
        return true;
    }
}
