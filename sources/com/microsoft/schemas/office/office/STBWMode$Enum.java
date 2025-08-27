package com.microsoft.schemas.office.office;

import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/office/STBWMode$Enum.class */
public final class STBWMode$Enum extends StringEnumAbstractBase {
    static final int INT_COLOR = 1;
    static final int INT_AUTO = 2;
    static final int INT_GRAY_SCALE = 3;
    static final int INT_LIGHT_GRAYSCALE = 4;
    static final int INT_INVERSE_GRAY = 5;
    static final int INT_GRAY_OUTLINE = 6;
    static final int INT_HIGH_CONTRAST = 7;
    static final int INT_BLACK = 8;
    static final int INT_WHITE = 9;
    static final int INT_HIDE = 10;
    static final int INT_UNDRAWN = 11;
    static final int INT_BLACK_TEXT_AND_LINES = 12;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STBWMode$Enum[]{new STBWMode$Enum("color", 1), new STBWMode$Enum("auto", 2), new STBWMode$Enum("grayScale", 3), new STBWMode$Enum("lightGrayscale", 4), new STBWMode$Enum("inverseGray", 5), new STBWMode$Enum("grayOutline", 6), new STBWMode$Enum("highContrast", 7), new STBWMode$Enum("black", 8), new STBWMode$Enum("white", 9), new STBWMode$Enum("hide", 10), new STBWMode$Enum("undrawn", 11), new STBWMode$Enum("blackTextAndLines", 12)});
    private static final long serialVersionUID = 1;

    public static STBWMode$Enum forString(String str) {
        return (STBWMode$Enum) table.forString(str);
    }

    public static STBWMode$Enum forInt(int i) {
        return (STBWMode$Enum) table.forInt(i);
    }

    private STBWMode$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
