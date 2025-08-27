package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/InterfaceHdrRecord.class */
public final class InterfaceHdrRecord extends StandardRecord {
    public static final short sid = 225;
    private final int _codepage;
    public static final int CODEPAGE = 1200;

    public InterfaceHdrRecord(int codePage) {
        this._codepage = codePage;
    }

    public InterfaceHdrRecord(RecordInputStream in) {
        this._codepage = in.readShort();
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[INTERFACEHDR]\n");
        buffer.append("    .codepage = ").append(HexDump.shortToHex(this._codepage)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/INTERFACEHDR]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        out.writeShort(this._codepage);
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 2;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 225;
    }
}
