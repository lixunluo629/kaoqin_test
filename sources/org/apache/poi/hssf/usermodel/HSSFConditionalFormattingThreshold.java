package org.apache.poi.hssf.usermodel;

import org.apache.poi.hssf.record.CFRuleBase;
import org.apache.poi.hssf.record.cf.Threshold;
import org.apache.poi.ss.usermodel.ConditionalFormattingThreshold;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/usermodel/HSSFConditionalFormattingThreshold.class */
public final class HSSFConditionalFormattingThreshold implements ConditionalFormattingThreshold {
    private final Threshold threshold;
    private final HSSFSheet sheet;
    private final HSSFWorkbook workbook;

    protected HSSFConditionalFormattingThreshold(Threshold threshold, HSSFSheet sheet) {
        this.threshold = threshold;
        this.sheet = sheet;
        this.workbook = sheet.getWorkbook();
    }

    protected Threshold getThreshold() {
        return this.threshold;
    }

    @Override // org.apache.poi.ss.usermodel.ConditionalFormattingThreshold
    public ConditionalFormattingThreshold.RangeType getRangeType() {
        return ConditionalFormattingThreshold.RangeType.byId(this.threshold.getType());
    }

    @Override // org.apache.poi.ss.usermodel.ConditionalFormattingThreshold
    public void setRangeType(ConditionalFormattingThreshold.RangeType type) {
        this.threshold.setType((byte) type.id);
    }

    @Override // org.apache.poi.ss.usermodel.ConditionalFormattingThreshold
    public String getFormula() {
        return HSSFConditionalFormattingRule.toFormulaString(this.threshold.getParsedExpression(), this.workbook);
    }

    @Override // org.apache.poi.ss.usermodel.ConditionalFormattingThreshold
    public void setFormula(String formula) {
        this.threshold.setParsedExpression(CFRuleBase.parseFormula(formula, this.sheet));
    }

    @Override // org.apache.poi.ss.usermodel.ConditionalFormattingThreshold
    public Double getValue() {
        return this.threshold.getValue();
    }

    @Override // org.apache.poi.ss.usermodel.ConditionalFormattingThreshold
    public void setValue(Double value) {
        this.threshold.setValue(value);
    }
}
