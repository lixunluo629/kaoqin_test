package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/PasswordRecord.class */
public final class PasswordRecord extends StandardRecord {
    public static final short sid = 19;
    private int field_1_password;

    public PasswordRecord(int password) {
        this.field_1_password = password;
    }

    public PasswordRecord(RecordInputStream in) {
        this.field_1_password = in.readShort();
    }

    public void setPassword(int password) {
        this.field_1_password = password;
    }

    public int getPassword() {
        return this.field_1_password;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[PASSWORD]\n");
        buffer.append("    .password = ").append(HexDump.shortToHex(this.field_1_password)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/PASSWORD]\n");
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
        return (short) 19;
    }

    @Override // org.apache.poi.hssf.record.Record
    public Object clone() {
        return new PasswordRecord(this.field_1_password);
    }
}
