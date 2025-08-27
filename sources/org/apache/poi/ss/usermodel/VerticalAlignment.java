package org.apache.poi.ss.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/VerticalAlignment.class */
public enum VerticalAlignment {
    TOP,
    CENTER,
    BOTTOM,
    JUSTIFY,
    DISTRIBUTED;

    public short getCode() {
        return (short) ordinal();
    }

    public static VerticalAlignment forInt(int code) {
        if (code < 0 || code >= values().length) {
            throw new IllegalArgumentException("Invalid VerticalAlignment code: " + code);
        }
        return values()[code];
    }
}
