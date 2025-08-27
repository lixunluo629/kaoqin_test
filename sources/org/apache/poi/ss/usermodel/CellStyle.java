package org.apache.poi.ss.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/CellStyle.class */
public interface CellStyle {
    short getIndex();

    void setDataFormat(short s);

    short getDataFormat();

    String getDataFormatString();

    void setFont(Font font);

    short getFontIndex();

    void setHidden(boolean z);

    boolean getHidden();

    void setLocked(boolean z);

    boolean getLocked();

    void setQuotePrefixed(boolean z);

    boolean getQuotePrefixed();

    void setAlignment(HorizontalAlignment horizontalAlignment);

    short getAlignment();

    HorizontalAlignment getAlignmentEnum();

    void setWrapText(boolean z);

    boolean getWrapText();

    void setVerticalAlignment(VerticalAlignment verticalAlignment);

    short getVerticalAlignment();

    VerticalAlignment getVerticalAlignmentEnum();

    void setRotation(short s);

    short getRotation();

    void setIndention(short s);

    short getIndention();

    void setBorderLeft(BorderStyle borderStyle);

    short getBorderLeft();

    BorderStyle getBorderLeftEnum();

    void setBorderRight(BorderStyle borderStyle);

    short getBorderRight();

    BorderStyle getBorderRightEnum();

    void setBorderTop(BorderStyle borderStyle);

    short getBorderTop();

    BorderStyle getBorderTopEnum();

    void setBorderBottom(BorderStyle borderStyle);

    short getBorderBottom();

    BorderStyle getBorderBottomEnum();

    void setLeftBorderColor(short s);

    short getLeftBorderColor();

    void setRightBorderColor(short s);

    short getRightBorderColor();

    void setTopBorderColor(short s);

    short getTopBorderColor();

    void setBottomBorderColor(short s);

    short getBottomBorderColor();

    void setFillPattern(FillPatternType fillPatternType);

    short getFillPattern();

    FillPatternType getFillPatternEnum();

    void setFillBackgroundColor(short s);

    short getFillBackgroundColor();

    Color getFillBackgroundColorColor();

    void setFillForegroundColor(short s);

    short getFillForegroundColor();

    Color getFillForegroundColorColor();

    void cloneStyleFrom(CellStyle cellStyle);

    void setShrinkToFit(boolean z);

    boolean getShrinkToFit();
}
