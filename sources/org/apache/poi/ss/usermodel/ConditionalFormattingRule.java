package org.apache.poi.ss.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/ConditionalFormattingRule.class */
public interface ConditionalFormattingRule extends DifferentialStyleProvider {
    BorderFormatting createBorderFormatting();

    @Override // org.apache.poi.ss.usermodel.DifferentialStyleProvider
    BorderFormatting getBorderFormatting();

    FontFormatting createFontFormatting();

    @Override // org.apache.poi.ss.usermodel.DifferentialStyleProvider
    FontFormatting getFontFormatting();

    PatternFormatting createPatternFormatting();

    @Override // org.apache.poi.ss.usermodel.DifferentialStyleProvider
    PatternFormatting getPatternFormatting();

    DataBarFormatting getDataBarFormatting();

    IconMultiStateFormatting getMultiStateFormatting();

    ColorScaleFormatting getColorScaleFormatting();

    @Override // org.apache.poi.ss.usermodel.DifferentialStyleProvider
    ExcelNumberFormat getNumberFormat();

    ConditionType getConditionType();

    ConditionFilterType getConditionFilterType();

    ConditionFilterData getFilterConfiguration();

    byte getComparisonOperation();

    String getFormula1();

    String getFormula2();

    int getPriority();

    boolean getStopIfTrue();
}
