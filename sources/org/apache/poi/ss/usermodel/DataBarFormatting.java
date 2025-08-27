package org.apache.poi.ss.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/DataBarFormatting.class */
public interface DataBarFormatting {
    boolean isLeftToRight();

    void setLeftToRight(boolean z);

    boolean isIconOnly();

    void setIconOnly(boolean z);

    int getWidthMin();

    void setWidthMin(int i);

    int getWidthMax();

    void setWidthMax(int i);

    Color getColor();

    void setColor(Color color);

    ConditionalFormattingThreshold getMinThreshold();

    ConditionalFormattingThreshold getMaxThreshold();
}
