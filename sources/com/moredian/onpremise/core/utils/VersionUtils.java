package com.moredian.onpremise.core.utils;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/VersionUtils.class */
public class VersionUtils {
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        while (index < minLen) {
            int i = Integer.parseInt(version1Array[index]) - Integer.parseInt(version2Array[index]);
            diff = i;
            if (i != 0) {
                break;
            }
            index++;
        }
        if (diff != 0) {
            return diff > 0 ? 1 : -1;
        }
        for (int i2 = index; i2 < version1Array.length; i2++) {
            if (Integer.parseInt(version1Array[i2]) > 0) {
                return 1;
            }
        }
        for (int i3 = index; i3 < version2Array.length; i3++) {
            if (Integer.parseInt(version2Array[i3]) > 0) {
                return -1;
            }
        }
        return 0;
    }
}
