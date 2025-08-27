package org.apache.poi.ss.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/DifferentialStyleProvider.class */
public interface DifferentialStyleProvider {
    BorderFormatting getBorderFormatting();

    FontFormatting getFontFormatting();

    ExcelNumberFormat getNumberFormat();

    PatternFormatting getPatternFormatting();

    int getStripeSize();
}
