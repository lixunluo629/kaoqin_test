package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/PasswordRev4Record.class */
public final class PasswordRev4Record extends StandardRecord {
    public static final short sid = 444;
    private int field_1_password;

    public PasswordRev4Record(int pw) {
        this.field_1_password = pw;
    }

    public PasswordRev4Record(RecordInputStream in) {
        this.field_1_password = in.readShort();
    }

    public void setPassword(short pw) {
        this.field_1_password = pw;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[PROT4REVPASSWORD]\n");
        buffer.append("    .password = ").append(HexDump.shortToHex(this.field_1_password)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/PROT4REVPASSWORD]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_password);
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 2;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 444;
    }
}
