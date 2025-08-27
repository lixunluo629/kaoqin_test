package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/DateWindow1904Record.class */
public final class DateWindow1904Record extends StandardRecord {
    public static final short sid = 34;
    private short field_1_window;

    public DateWindow1904Record() {
    }

    public DateWindow1904Record(RecordInputStream in) {
        this.field_1_window = in.readShort();
    }

    public void setWindowing(short window) {
        this.field_1_window = window;
    }

    public short getWindowing() {
        return this.field_1_window;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[1904]\n");
        buffer.append("    .is1904          = ").append(Integer.toHexString(getWindowing())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/1904]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        out.writeShort(getWindowing());
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 2;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 34;
    }
}
