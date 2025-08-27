package org.apache.poi.ss.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/BorderFormatting.class */
public interface BorderFormatting {
    short getBorderBottom();

    BorderStyle getBorderBottomEnum();

    short getBorderDiagonal();

    BorderStyle getBorderDiagonalEnum();

    short getBorderLeft();

    BorderStyle getBorderLeftEnum();

    short getBorderRight();

    BorderStyle getBorderRightEnum();

    short getBorderTop();

    BorderStyle getBorderTopEnum();

    BorderStyle getBorderVerticalEnum();

    BorderStyle getBorderHorizontalEnum();

    short getBottomBorderColor();

    Color getBottomBorderColorColor();

    short getDiagonalBorderColor();

    Color getDiagonalBorderColorColor();

    short getLeftBorderColor();

    Color getLeftBorderColorColor();

    short getRightBorderColor();

    Color getRightBorderColorColor();

    short getTopBorderColor();

    Color getTopBorderColorColor();

    short getVerticalBorderColor();

    Color getVerticalBorderColorColor();

    short getHorizontalBorderColor();

    Color getHorizontalBorderColorColor();

    void setBorderBottom(short s);

    void setBorderBottom(BorderStyle borderStyle);

    void setBorderDiagonal(short s);

    void setBorderDiagonal(BorderStyle borderStyle);

    void setBorderLeft(short s);

    void setBorderLeft(BorderStyle borderStyle);

    void setBorderRight(short s);

    void setBorderRight(BorderStyle borderStyle);

    void setBorderTop(short s);

    void setBorderTop(BorderStyle borderStyle);

    void setBorderHorizontal(BorderStyle borderStyle);

    void setBorderVertical(BorderStyle borderStyle);

    void setBottomBorderColor(short s);

    void setBottomBorderColor(Color color);

    void setDiagonalBorderColor(short s);

    void setDiagonalBorderColor(Color color);

    void setLeftBorderColor(short s);

    void setLeftBorderColor(Color color);

    void setRightBorderColor(short s);

    void setRightBorderColor(Color color);

    void setTopBorderColor(short s);

    void setTopBorderColor(Color color);

    void setHorizontalBorderColor(short s);

    void setHorizontalBorderColor(Color color);

    void setVerticalBorderColor(short s);

    void setVerticalBorderColor(Color color);
}
