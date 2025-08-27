package com.itextpdf.io.util;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/util/ExceptionUtil.class */
public final class ExceptionUtil {
    private ExceptionUtil() {
    }

    public static boolean isOutOfRange(Exception e) {
        return e instanceof IndexOutOfBoundsException;
    }
}
