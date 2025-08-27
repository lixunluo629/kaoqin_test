package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/FnGroupCountRecord.class */
public final class FnGroupCountRecord extends StandardRecord {
    public static final short sid = 156;
    public static final short COUNT = 14;
    private short field_1_count;

    public FnGroupCountRecord() {
    }

    public FnGroupCountRecord(RecordInputStream in) {
        this.field_1_count = in.readShort();
    }

    public void setCount(short count) {
        this.field_1_count = count;
    }

    public short getCount() {
        return this.field_1_count;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FNGROUPCOUNT]\n");
        buffer.append("    .count            = ").append((int) getCount()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/FNGROUPCOUNT]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        out.writeShort(getCount());
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 2;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 156;
    }
}
