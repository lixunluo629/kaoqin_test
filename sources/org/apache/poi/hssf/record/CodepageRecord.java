package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/CodepageRecord.class */
public final class CodepageRecord extends StandardRecord {
    public static final short sid = 66;
    private short field_1_codepage;
    public static final short CODEPAGE = 1200;

    public CodepageRecord() {
    }

    public CodepageRecord(RecordInputStream in) {
        this.field_1_codepage = in.readShort();
    }

    public void setCodepage(short cp) {
        this.field_1_codepage = cp;
    }

    public short getCodepage() {
        return this.field_1_codepage;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[CODEPAGE]\n");
        buffer.append("    .codepage        = ").append(Integer.toHexString(getCodepage())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/CODEPAGE]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        out.writeShort(getCodepage());
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 2;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 66;
    }
}
