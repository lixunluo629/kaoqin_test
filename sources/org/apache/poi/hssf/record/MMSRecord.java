package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/MMSRecord.class */
public final class MMSRecord extends StandardRecord {
    public static final short sid = 193;
    private byte field_1_addMenuCount;
    private byte field_2_delMenuCount;

    public MMSRecord() {
    }

    public MMSRecord(RecordInputStream in) {
        if (in.remaining() == 0) {
            return;
        }
        this.field_1_addMenuCount = in.readByte();
        this.field_2_delMenuCount = in.readByte();
    }

    public void setAddMenuCount(byte am) {
        this.field_1_addMenuCount = am;
    }

    public void setDelMenuCount(byte dm) {
        this.field_2_delMenuCount = dm;
    }

    public byte getAddMenuCount() {
        return this.field_1_addMenuCount;
    }

    public byte getDelMenuCount() {
        return this.field_2_delMenuCount;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[MMS]\n");
        buffer.append("    .addMenu        = ").append(Integer.toHexString(getAddMenuCount())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("    .delMenu        = ").append(Integer.toHexString(getDelMenuCount())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/MMS]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        out.writeByte(getAddMenuCount());
        out.writeByte(getDelMenuCount());
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 2;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 193;
    }
}
