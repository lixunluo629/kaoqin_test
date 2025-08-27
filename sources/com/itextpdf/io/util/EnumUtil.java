package com.itextpdf.io.util;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/util/EnumUtil.class */
public final class EnumUtil {
    private EnumUtil() {
    }

    public static <T extends Enum<T>> T throwIfNull(T enumInstance) {
        if (enumInstance == null) {
            throw new RuntimeException("Expected not null enum instance");
        }
        return enumInstance;
    }
}
