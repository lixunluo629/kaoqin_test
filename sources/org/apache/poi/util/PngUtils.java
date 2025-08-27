package org.apache.poi.util;

/* loaded from: poi-3.17.jar:org/apache/poi/util/PngUtils.class */
public final class PngUtils {
    private static final byte[] PNG_FILE_HEADER = {-119, 80, 78, 71, 13, 10, 26, 10};

    private PngUtils() {
    }

    public static boolean matchesPngHeader(byte[] data, int offset) {
        if (data == null || data.length - offset < PNG_FILE_HEADER.length) {
            return false;
        }
        for (int i = 0; i < PNG_FILE_HEADER.length; i++) {
            if (PNG_FILE_HEADER[i] != data[i + offset]) {
                return false;
            }
        }
        return true;
    }
}
