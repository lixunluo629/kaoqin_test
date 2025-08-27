package org.apache.poi.hssf.util;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/RKUtil.class */
public final class RKUtil {
    private RKUtil() {
    }

    public static double decodeNumber(int number) {
        double rvalue;
        long raw_number = number >> 2;
        if ((number & 2) == 2) {
            rvalue = raw_number;
        } else {
            rvalue = Double.longBitsToDouble(raw_number << 34);
        }
        if ((number & 1) == 1) {
            rvalue /= 100.0d;
        }
        return rvalue;
    }
}
