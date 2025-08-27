package org.apache.poi.ss.usermodel;

import org.apache.poi.ss.util.CellRangeAddress;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/ConditionalFormatting.class */
public interface ConditionalFormatting {
    CellRangeAddress[] getFormattingRanges();

    void setFormattingRanges(CellRangeAddress[] cellRangeAddressArr);

    void setRule(int i, ConditionalFormattingRule conditionalFormattingRule);

    void addRule(ConditionalFormattingRule conditionalFormattingRule);

    ConditionalFormattingRule getRule(int i);

    int getNumberOfRules();
}
