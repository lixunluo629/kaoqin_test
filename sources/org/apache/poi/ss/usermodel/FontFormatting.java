package org.apache.poi.ss.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/FontFormatting.class */
public interface FontFormatting {
    public static final short SS_NONE = 0;
    public static final short SS_SUPER = 1;
    public static final short SS_SUB = 2;
    public static final byte U_NONE = 0;
    public static final byte U_SINGLE = 1;
    public static final byte U_DOUBLE = 2;
    public static final byte U_SINGLE_ACCOUNTING = 33;
    public static final byte U_DOUBLE_ACCOUNTING = 34;

    short getEscapementType();

    void setEscapementType(short s);

    short getFontColorIndex();

    void setFontColorIndex(short s);

    Color getFontColor();

    void setFontColor(Color color);

    int getFontHeight();

    void setFontHeight(int i);

    short getUnderlineType();

    void setUnderlineType(short s);

    boolean isBold();

    boolean isItalic();

    boolean isStruckout();

    void setFontStyle(boolean z, boolean z2);

    void resetFontStyle();
}
