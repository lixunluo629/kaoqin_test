package org.apache.poi.ss.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/ColorScaleFormatting.class */
public interface ColorScaleFormatting {
    int getNumControlPoints();

    void setNumControlPoints(int i);

    Color[] getColors();

    void setColors(Color[] colorArr);

    ConditionalFormattingThreshold[] getThresholds();

    void setThresholds(ConditionalFormattingThreshold[] conditionalFormattingThresholdArr);

    ConditionalFormattingThreshold createThreshold();
}
