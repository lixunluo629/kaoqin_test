package org.apache.poi.ss.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/Font.class */
public interface Font {
    public static final short COLOR_NORMAL = Short.MAX_VALUE;
    public static final short COLOR_RED = 10;
    public static final short SS_NONE = 0;
    public static final short SS_SUPER = 1;
    public static final short SS_SUB = 2;
    public static final byte U_NONE = 0;
    public static final byte U_SINGLE = 1;
    public static final byte U_DOUBLE = 2;
    public static final byte U_SINGLE_ACCOUNTING = 33;
    public static final byte U_DOUBLE_ACCOUNTING = 34;
    public static final byte ANSI_CHARSET = 0;
    public static final byte DEFAULT_CHARSET = 1;
    public static final byte SYMBOL_CHARSET = 2;

    void setFontName(String str);

    String getFontName();

    void setFontHeight(short s);

    void setFontHeightInPoints(short s);

    short getFontHeight();

    short getFontHeightInPoints();

    void setItalic(boolean z);

    boolean getItalic();

    void setStrikeout(boolean z);

    boolean getStrikeout();

    void setColor(short s);

    short getColor();

    void setTypeOffset(short s);

    short getTypeOffset();

    void setUnderline(byte b);

    byte getUnderline();

    int getCharSet();

    void setCharSet(byte b);

    void setCharSet(int i);

    short getIndex();

    void setBold(boolean z);

    boolean getBold();
}
