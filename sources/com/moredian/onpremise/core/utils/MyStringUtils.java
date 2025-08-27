package com.moredian.onpremise.core.utils;

import java.util.List;
import org.apache.commons.lang.StringUtils;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/MyStringUtils.class */
public class MyStringUtils extends StringUtils {
    public static String replaceForString(String var, List<String> var2) {
        for (String var3 : var2) {
            if (var.contains("," + var3 + ",")) {
                var = var.replace("," + var3 + ",", ",");
            } else if (var.contains("," + var3)) {
                var = var.replace("," + var3, "");
            } else if (var.contains(var3 + ",")) {
                var = var.replace(var3 + ",", "");
            } else if (var.contains(var3)) {
                var = var.replace(var3, "");
            }
        }
        return var;
    }

    public static String replaceForLong(String var, List<Long> var2) {
        for (Long var3 : var2) {
            if (var.contains("," + var3 + ",")) {
                var = var.replace("," + var3 + ",", ",");
            } else if (var.contains("," + var3)) {
                var = var.replace("," + var3, "");
            } else if (var.contains(var3 + ",")) {
                var = var.replace(var3 + ",", "");
            } else if (var.contains(String.valueOf(var3))) {
                var = var.replace(String.valueOf(var3), "");
            }
        }
        return var;
    }

    public static String appendForString(String var, List<String> var2) {
        for (String var3 : var2) {
            if (!var.contains(var3)) {
                var = var + "," + var3;
            }
        }
        return var;
    }

    public static String appendForLong(String var, List<Long> var2) {
        for (Long var3 : var2) {
            if (!var.contains(String.valueOf(var3))) {
                var = var + "," + var3;
            }
        }
        return var;
    }
}
