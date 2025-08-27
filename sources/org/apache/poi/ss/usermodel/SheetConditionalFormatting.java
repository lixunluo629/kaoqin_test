package org.apache.poi.ss.usermodel;

import org.apache.poi.ss.usermodel.IconMultiStateFormatting;
import org.apache.poi.ss.util.CellRangeAddress;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/SheetConditionalFormatting.class */
public interface SheetConditionalFormatting {
    int addConditionalFormatting(CellRangeAddress[] cellRangeAddressArr, ConditionalFormattingRule conditionalFormattingRule);

    int addConditionalFormatting(CellRangeAddress[] cellRangeAddressArr, ConditionalFormattingRule conditionalFormattingRule, ConditionalFormattingRule conditionalFormattingRule2);

    int addConditionalFormatting(CellRangeAddress[] cellRangeAddressArr, ConditionalFormattingRule[] conditionalFormattingRuleArr);

    int addConditionalFormatting(ConditionalFormatting conditionalFormatting);

    ConditionalFormattingRule createConditionalFormattingRule(byte b, String str, String str2);

    ConditionalFormattingRule createConditionalFormattingRule(byte b, String str);

    ConditionalFormattingRule createConditionalFormattingRule(String str);

    ConditionalFormattingRule createConditionalFormattingRule(ExtendedColor extendedColor);

    ConditionalFormattingRule createConditionalFormattingRule(IconMultiStateFormatting.IconSet iconSet);

    ConditionalFormattingRule createConditionalFormattingColorScaleRule();

    ConditionalFormatting getConditionalFormattingAt(int i);

    int getNumConditionalFormattings();

    void removeConditionalFormatting(int i);
}
