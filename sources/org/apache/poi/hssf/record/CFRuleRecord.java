package org.apache.poi.hssf.record;

import java.util.Arrays;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.formula.Formula;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.RecordFormatException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/CFRuleRecord.class */
public final class CFRuleRecord extends CFRuleBase implements Cloneable {
    public static final short sid = 433;

    private CFRuleRecord(byte conditionType, byte comparisonOperation) {
        super(conditionType, comparisonOperation);
        setDefaults();
    }

    private CFRuleRecord(byte conditionType, byte comparisonOperation, Ptg[] formula1, Ptg[] formula2) {
        super(conditionType, comparisonOperation, formula1, formula2);
        setDefaults();
    }

    private void setDefaults() {
        this.formatting_options = modificationBits.setValue(this.formatting_options, -1);
        this.formatting_options = fmtBlockBits.setValue(this.formatting_options, 0);
        this.formatting_options = undocumented.clear(this.formatting_options);
        this.formatting_not_used = (short) -32766;
        this._fontFormatting = null;
        this._borderFormatting = null;
        this._patternFormatting = null;
    }

    public static CFRuleRecord create(HSSFSheet sheet, String formulaText) {
        Ptg[] formula1 = parseFormula(formulaText, sheet);
        return new CFRuleRecord((byte) 2, (byte) 0, formula1, null);
    }

    public static CFRuleRecord create(HSSFSheet sheet, byte comparisonOperation, String formulaText1, String formulaText2) {
        Ptg[] formula1 = parseFormula(formulaText1, sheet);
        Ptg[] formula2 = parseFormula(formulaText2, sheet);
        return new CFRuleRecord((byte) 1, comparisonOperation, formula1, formula2);
    }

    public CFRuleRecord(RecordInputStream in) throws RecordFormatException {
        setConditionType(in.readByte());
        setComparisonOperation(in.readByte());
        int field_3_formula1_len = in.readUShort();
        int field_4_formula2_len = in.readUShort();
        readFormatOptions(in);
        setFormula1(Formula.read(field_3_formula1_len, in));
        setFormula2(Formula.read(field_4_formula2_len, in));
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 433;
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        int formula1Len = getFormulaSize(getFormula1());
        int formula2Len = getFormulaSize(getFormula2());
        out.writeByte(getConditionType());
        out.writeByte(getComparisonOperation());
        out.writeShort(formula1Len);
        out.writeShort(formula2Len);
        serializeFormattingBlock(out);
        getFormula1().serializeTokens(out);
        getFormula2().serializeTokens(out);
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 6 + getFormattingBlockSize() + getFormulaSize(getFormula1()) + getFormulaSize(getFormula2());
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[CFRULE]\n");
        buffer.append("    .condition_type   =").append((int) getConditionType()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("    OPTION FLAGS=0x").append(Integer.toHexString(getOptions())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        if (containsFontFormattingBlock()) {
            buffer.append(this._fontFormatting).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        if (containsBorderFormattingBlock()) {
            buffer.append(this._borderFormatting).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        if (containsPatternFormattingBlock()) {
            buffer.append(this._patternFormatting).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        buffer.append("    Formula 1 =").append(Arrays.toString(getFormula1().getTokens())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("    Formula 2 =").append(Arrays.toString(getFormula2().getTokens())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/CFRULE]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.CFRuleBase, org.apache.poi.hssf.record.Record
    public CFRuleRecord clone() {
        CFRuleRecord rec = new CFRuleRecord(getConditionType(), getComparisonOperation());
        super.copyTo(rec);
        return rec;
    }
}
