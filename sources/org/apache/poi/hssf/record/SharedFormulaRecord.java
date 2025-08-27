package org.apache.poi.hssf.record;

import org.apache.poi.hssf.util.CellRangeAddress8Bit;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.Formula;
import org.apache.poi.ss.formula.SharedFormula;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.RecordFormatException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/SharedFormulaRecord.class */
public final class SharedFormulaRecord extends SharedValueRecordBase {
    public static final short sid = 1212;
    private int field_5_reserved;
    private Formula field_7_parsed_expr;

    public SharedFormulaRecord() {
        this(new CellRangeAddress8Bit(0, 0, 0, 0));
    }

    private SharedFormulaRecord(CellRangeAddress8Bit range) {
        super(range);
        this.field_7_parsed_expr = Formula.create(Ptg.EMPTY_PTG_ARRAY);
    }

    public SharedFormulaRecord(RecordInputStream in) throws RecordFormatException {
        super(in);
        this.field_5_reserved = in.readShort();
        int field_6_expression_len = in.readShort();
        int nAvailableBytes = in.available();
        this.field_7_parsed_expr = Formula.read(field_6_expression_len, in, nAvailableBytes);
    }

    @Override // org.apache.poi.hssf.record.SharedValueRecordBase
    protected void serializeExtraData(LittleEndianOutput out) {
        out.writeShort(this.field_5_reserved);
        this.field_7_parsed_expr.serialize(out);
    }

    @Override // org.apache.poi.hssf.record.SharedValueRecordBase
    protected int getExtraDataSize() {
        return 2 + this.field_7_parsed_expr.getEncodedSize();
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[SHARED FORMULA (").append(HexDump.intToHex(1212)).append("]\n");
        buffer.append("    .range      = ").append(getRange()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("    .reserved    = ").append(HexDump.shortToHex(this.field_5_reserved)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        Ptg[] ptgs = this.field_7_parsed_expr.getTokens();
        for (int k = 0; k < ptgs.length; k++) {
            buffer.append("Formula[").append(k).append("]");
            Ptg ptg = ptgs[k];
            buffer.append(ptg).append(ptg.getRVAType()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        buffer.append("[/SHARED FORMULA]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 1212;
    }

    public Ptg[] getFormulaTokens(FormulaRecord formula) {
        int formulaRow = formula.getRow();
        int formulaColumn = formula.getColumn();
        if (!isInRange(formulaRow, formulaColumn)) {
            throw new RuntimeException("Shared Formula Conversion: Coding Error");
        }
        SharedFormula sf = new SharedFormula(SpreadsheetVersion.EXCEL97);
        return sf.convertSharedFormulas(this.field_7_parsed_expr.getTokens(), formulaRow, formulaColumn);
    }

    @Override // org.apache.poi.hssf.record.Record
    public Object clone() {
        SharedFormulaRecord result = new SharedFormulaRecord(getRange());
        result.field_5_reserved = this.field_5_reserved;
        result.field_7_parsed_expr = this.field_7_parsed_expr.copy();
        return result;
    }

    public boolean isFormulaSame(SharedFormulaRecord other) {
        return this.field_7_parsed_expr.isSame(other.field_7_parsed_expr);
    }
}
